/**
 * 
 */
package com.bangalorewest.bagtracker.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bangalorewest.bagtracker.constants.MessageBuilderConstants;
import com.bangalorewest.bagtracker.dto.BagEvent;
import com.bangalorewest.bagtracker.dto.BagHistoryDTO;
import com.bangalorewest.bagtracker.dto.LoadUnloadBag;
import com.bangalorewest.bagtracker.dto.PaxItinerary;
import com.bangalorewest.bagtracker.exception.InvalidBagTagException;
import com.bangalorewest.bagtracker.util.BagTrackUtil;
import com.bangalorewest.bagtracker.util.DateTimeUtil;

/**
 * @author sudhanshu.singh
 *
 */
@Component
public class MessageBuilder {

	@Autowired
	BookingDAO bookingDAO;

	public String buildBSM(PaxItinerary paxItinerary, String airport, 
			String bagActionType, int index) {
		StringBuilder bsm = new StringBuilder();
		buildMsgType(bsm, true); // BSM or BPM
		buildVElement(paxItinerary, bsm, airport, bagActionType); // .V element
		buildSegments(paxItinerary, bsm, airport, bagActionType);
		//buildOnlyCurrentSegment(paxItinerary, bsm); // .F element
		//buildOnlyOnwardSegments(paxItinerary, bsm); // .O element
		buildNElement(paxItinerary, bsm, MessageBuilderConstants.BSM_MESSAGE_TYPE.getMessage()); // BagTag generation
		buildSElement(paxItinerary, bsm, MessageBuilderConstants.CHECKEDIN.getMessage());
		buildPElement(paxItinerary, bsm); // Pax Name
		buildEndMsg(bsm, true); // ENDBSM or ENDBPM
		return bsm.toString();

	}

	private void buildSElement(PaxItinerary paxItinerary, StringBuilder bsm, String operation) {
		bsm.append(MessageBuilderConstants.DOT_S.getMessage()).append(operation).append(System.lineSeparator());
	}

	private void buildSegments(PaxItinerary paxItinerary, StringBuilder bsm, String airport, 
			String bagActionType) {
		//local
		if (MessageBuilderConstants.LOCAL_BAG.getMessage().equalsIgnoreCase(bagActionType)) {
			buildOnlyCurrentSegment(paxItinerary, bsm); // .F element
			buildOnlyOnwardSegments(paxItinerary, bsm); // .O element
		}
		//transfer
		else if (MessageBuilderConstants.TRANSFER_BAG.getMessage().equalsIgnoreCase(bagActionType)) {
			buildFElement(paxItinerary, bsm, airport, MessageBuilderConstants.TRANSFER_BAG.getMessage());
			buildOnlyInboundSegment(paxItinerary, bsm, airport);
			buildOElement(paxItinerary, bsm, airport);
		}
		//terminate
		else {
			buildOnlyInboundSegmentForTerminateBag(paxItinerary, bsm, airport);
		}
	}

	private void buildOnlyInboundSegmentForTerminateBag(PaxItinerary paxItinerary, StringBuilder bsm, String airport) {
		String[] splitSegments = splitSegments(paxItinerary);
		String inboundSegment = splitSegments[splitSegments.length - 1];
		bsm.append(MessageBuilderConstants.DOT_I.getMessage());
		String[] segment = splitFlights(inboundSegment, MessageBuilderConstants.HYPHEN.getMessage());
		buildSegment(bsm, segment, MessageBuilderConstants.ELEMENT_I.getMessage());
	}

	private void buildOElement(PaxItinerary paxItinerary, StringBuilder bsm, String airport) {
		String[] splitSegments = splitSegments(paxItinerary);
		int onwardSegmentStartIndex = getOnwardSegmentStartIndex(splitSegments, airport);
		if (onwardSegmentStartIndex > 0 && onwardSegmentStartIndex <=  splitSegments.length) {
			buildOnlyOnwardSegments(splitSegments, bsm, onwardSegmentStartIndex);
		}
	}

	private int getOnwardSegmentStartIndex(String[] splitSegments, String airport) {
		int index = 0;
		for (String segment : splitSegments) {
			String[] splittedFlight = splitFlights(segment, MessageBuilderConstants.HYPHEN.getMessage());
			//match departure airport of segment
			if (splittedFlight[2].equalsIgnoreCase(airport)) {
				index++;
				break;
			}
			index++;
		}
		return index;
	}

	private void buildFElement(PaxItinerary paxItinerary, StringBuilder bsm, String airport, String bagType) {
		String[] splitSegments = splitSegments(paxItinerary);
		String flightSegment = null;
		if (bagType.equalsIgnoreCase(MessageBuilderConstants.TERMINATE_BAG.getMessage())) {
			flightSegment = getFlightSegment(splitSegments, airport, MessageBuilderConstants.ELEMENT_I.getMessage());
		}
		else {
			flightSegment = getFlightSegment(splitSegments, airport, MessageBuilderConstants.ELEMENT_F.getMessage());
		}
		bsm.append(MessageBuilderConstants.DOT_F.getMessage());
		String[] segment = splitFlights(flightSegment, MessageBuilderConstants.HYPHEN.getMessage());
		buildSegment(bsm, segment, null);
	}

	private String getFlightSegment(String[] splitSegments, String airport, String element) {
		for (String segment : splitSegments) {
			String[] splitFlights = splitFlights(segment, MessageBuilderConstants.HYPHEN.getMessage());
			
			if (element.equalsIgnoreCase(MessageBuilderConstants.ELEMENT_F.getMessage())) {
				//match departure airport of segment
				if (splitFlights[2].equalsIgnoreCase(airport)) {
					return segment;
				}
			}
			else if (element.equalsIgnoreCase(MessageBuilderConstants.ELEMENT_I.getMessage())) {
				//match arrival airport of segment
				if (splitFlights[3].equalsIgnoreCase(airport)) {
					return segment;
				}
			}
		}
		return null;
	}
	
	private String[] splitFlights(String segment, String splitter) {
		String[] splitFlights = segment.split(splitter);
		return splitFlights;
	}

	private void buildOnlyInboundSegment(PaxItinerary paxItinerary, StringBuilder bsm, String airport) {
		String[] splitSegments = splitSegments(paxItinerary);
		String flightSegment = getFlightSegment(splitSegments, airport, 
				MessageBuilderConstants.ELEMENT_I.getMessage());
		if (flightSegment != null) {
			bsm.append(MessageBuilderConstants.DOT_I.getMessage());
			String[] segment = splitFlights(flightSegment, MessageBuilderConstants.HYPHEN.getMessage());
			buildSegment(bsm, segment, MessageBuilderConstants.ELEMENT_I.getMessage());
		}
	}

	public String buildBPMForLoad(LoadUnloadBag loadUnloadBag, BagEvent bagEvent) throws InvalidBagTagException {
		StringBuilder bpm = new StringBuilder();
		buildMsgType(bpm, false);
		List<PaxItinerary> paxItineraries = bookingDAO.getBookings(loadUnloadBag.getDateOfTravel());
		PaxItinerary validPaxItinerary = validateBagTagAndFilterItineraries(loadUnloadBag, paxItineraries);
		if (validPaxItinerary != null) {
			String date = validPaxItinerary.getDateOfTravel().substring(0, 2);
			String month = validPaxItinerary.getDateOfTravel().substring(2, 5);
			String year = validPaxItinerary.getDateOfTravel().substring(5);
			bagEvent.setBagDate(BagTrackUtil.monthInNumber(month) + "/" +  date + "/" + year);
			bagEvent.setFrom(validPaxItinerary.getLoggedInAgent());
			bagEvent.setFlight(validPaxItinerary.getFlight());
		}
		buildVElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation(),
				MessageBuilderConstants.LOCAL_BAG.getMessage());
		//build .J element
		buildJElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation(), MessageBuilderConstants.SECONDARY_CODE_S.getMessage(), 
				MessageBuilderConstants.SCREENING_DEVICE_2.getMessage(), MessageBuilderConstants.READING_LOCATION_2.getMessage(), 
				MessageBuilderConstants.TRANSFER_BAG.getMessage());
		//buildOnlyCurrentSegment(validPaxItinerary, bpm);
		buildFElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation(), MessageBuilderConstants.TRANSFER_BAG.getMessage());
		buildUElement(loadUnloadBag, bpm); // ULD Container
		buildNElement(validPaxItinerary, bpm, MessageBuilderConstants.BPM_MESSAGE_TYPE.getMessage());
		buildOnlyInboundSegment(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation());
		//buildOnlyOnwardSegments(validPaxItinerary, bpm);
		buildOElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation());
		buildSElement(validPaxItinerary, bpm, MessageBuilderConstants.BOARDED.getMessage());
		buildPElement(validPaxItinerary, bpm);
		buildEndMsg(bpm, false);
		return bpm.toString();
	}

	private void buildJElement(PaxItinerary validPaxItinerary, StringBuilder bpm, String agentLocation, 
			String secondaryCode, String scannerDevice, String readingLocation, String bagType) {
		String[] splitSegments = splitSegments(validPaxItinerary);
		String flightSegment = null;
		if (bagType.equalsIgnoreCase(MessageBuilderConstants.TERMINATE_BAG.getMessage())) {
			flightSegment = getFlightSegment(splitSegments, agentLocation, MessageBuilderConstants.ELEMENT_I.getMessage());
		}
		else {
			flightSegment = getFlightSegment(splitSegments, agentLocation, MessageBuilderConstants.ELEMENT_F.getMessage());
		}
		
		String[] splitFlights = splitFlights(flightSegment, MessageBuilderConstants.HYPHEN.getMessage());
		bpm.append(MessageBuilderConstants.DOT_J.getMessage()).append(secondaryCode).append("//").append(scannerDevice)
		.append(MessageBuilderConstants.FWD_SLASH.getMessage()).append(splitFlights[1].substring(0, 5))
		.append(MessageBuilderConstants.FWD_SLASH.getMessage()).append(DateTimeUtil.getCurrentTime("HHmmss"))
		.append(MessageBuilderConstants.FWD_SLASH.getMessage()).append(readingLocation).append(System.lineSeparator());
	}

	private PaxItinerary validateBagTagAndFilterItineraries(LoadUnloadBag loadUnloadBag,
			List<PaxItinerary> paxItineraries) throws InvalidBagTagException {
		PaxItinerary validPaxItinerary = null;
		for (PaxItinerary paxItinerary : paxItineraries) {
			if (paxItinerary.getListOfGeneratedBagTags() != null) {
				for (String bagTag : paxItinerary.getListOfGeneratedBagTags()) {
					if (bagTag != null && bagTag.equalsIgnoreCase(loadUnloadBag.getBagTagID())
							&& paxItinerary.getDateOfTravel().equalsIgnoreCase(loadUnloadBag.getDateOfTravel())) {
						validPaxItinerary = paxItinerary;
					}
				}
			}
		}
		if (validPaxItinerary == null) {
			throw new InvalidBagTagException("Invalid BagTag Passed");
		}
		return validPaxItinerary;
	}

	public String buildBPMForUnload(LoadUnloadBag loadUnloadRequest, BagEvent bagEvent, 
			Map<String, List<BagHistoryDTO>> bagHistoriesMap) throws InvalidBagTagException {
		StringBuilder bpm = new StringBuilder();
		buildMsgType(bpm, false);
		List<PaxItinerary> paxItineraries = bookingDAO.getBookings(loadUnloadRequest.getDateOfTravel());
		PaxItinerary validPaxItinerary = validateBagTagAndFilterItineraries(loadUnloadRequest, paxItineraries);
		if (validPaxItinerary != null) {
			String date = validPaxItinerary.getDateOfTravel().substring(0, 2);
			String month = validPaxItinerary.getDateOfTravel().substring(2, 5);
			String year = validPaxItinerary.getDateOfTravel().substring(5);
			bagEvent.setBagDate(BagTrackUtil.monthInNumber(month) + "/" +  date + "/" + year);
			bagEvent.setFrom(validPaxItinerary.getLoggedInAgent());
			bagEvent.setFlight(validPaxItinerary.getFlight());
		}

		buildVElement(validPaxItinerary, bpm, loadUnloadRequest.getAgentLocation(), 
				MessageBuilderConstants.LOCAL_BAG.getMessage());
		buildJElement(validPaxItinerary, bpm, loadUnloadRequest.getAgentLocation(), MessageBuilderConstants.SECONDARY_CODE_S.getMessage(), 
				MessageBuilderConstants.SCREENING_DEVICE_2.getMessage(), MessageBuilderConstants.READING_LOCATION_2.getMessage(), 
				MessageBuilderConstants.TRANSFER_BAG.getMessage());
		//buildOnlyCurrentSegment(validPaxItinerary, bpm);
		buildFElement(validPaxItinerary, bpm, loadUnloadRequest.getAgentLocation(), MessageBuilderConstants.TRANSFER_BAG.getMessage());
		buildUElementUnload(loadUnloadRequest, bpm, bagHistoriesMap);
		buildBElement(validPaxItinerary, bpm);
		buildOnlyInboundSegment(validPaxItinerary, bpm, loadUnloadRequest.getAgentLocation());
		buildOElement(validPaxItinerary, bpm, loadUnloadRequest.getAgentLocation());
		buildSElement(validPaxItinerary, bpm, MessageBuilderConstants.BOARDED.getMessage());
		buildPElement(validPaxItinerary, bpm);
		buildEndMsg(bpm, false);

		return bpm.toString();
	}
	
	public PaxItinerary getValidPaxItinerary(String bagTagID, String dateOfTravel) throws InvalidBagTagException {
		LoadUnloadBag  loadUnloadRequest = new LoadUnloadBag();
		loadUnloadRequest.setBagTagID(bagTagID);
		loadUnloadRequest.setDateOfTravel(dateOfTravel);
		List<PaxItinerary> paxItineraries = bookingDAO.getBookings(loadUnloadRequest.getDateOfTravel());
		PaxItinerary validPaxItinerary = validateBagTagAndFilterItineraries(loadUnloadRequest, paxItineraries);
		return validPaxItinerary;
	}

	private void buildBElement(PaxItinerary paxItinerary, StringBuilder bpm) {
		bpm.append(MessageBuilderConstants.DOT_B.getMessage()).append("OFF")
				.append(MessageBuilderConstants.FWD_SLASH.getMessage())
				.append(paxItinerary.getListOfGeneratedBagTags().get(0)).append("00")
				.append(new Integer(1)).append(System.lineSeparator());
	}

	private void buildUElement(LoadUnloadBag loadUnloadRequest, StringBuilder bpm) {
		bpm.append(MessageBuilderConstants.DOT_U.getMessage()).append(loadUnloadRequest.getUldContainer())
				.append(System.lineSeparator());
	}
	
	private void buildUElementUnload(LoadUnloadBag loadUnloadRequest, StringBuilder bpm, Map<String, 
			List<BagHistoryDTO>> bagHistoriesMap) {
		bpm.append(MessageBuilderConstants.DOT_U.getMessage()).append(
				getContainer(loadUnloadRequest.getAgentLocation(), bagHistoriesMap))
				.append(System.lineSeparator());
	}

	private String getContainer(String agentLocation, Map<String, List<BagHistoryDTO>> bagHistoriesMap) {
		String containerID = null;
		if (bagHistoriesMap != null && !bagHistoriesMap.isEmpty()) {
			for (Map.Entry<String, List<BagHistoryDTO>> entry : bagHistoriesMap.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null && entry.getKey().equalsIgnoreCase(agentLocation)) {
					for (BagHistoryDTO bagHistoryDTO : entry.getValue()) {
						if (bagHistoryDTO.getMessage() != null 
								&& bagHistoryDTO.getMessage().contains(MessageBuilderConstants.DOT_U.getMessage()) 
								&& !bagHistoryDTO.getMessage().contains(MessageBuilderConstants.DOT_B.getMessage())) {
							String[] splittedContainerMessage = bagHistoryDTO.getMessage().split(MessageBuilderConstants.DOT_U.getMessage());
							if (splittedContainerMessage != null && splittedContainerMessage.length >= 2) {
								String[] splittedSubContainerMessage = splittedContainerMessage[1].split(MessageBuilderConstants.DOT_N.getMessage());
								if (splittedSubContainerMessage != null && splittedSubContainerMessage.length >= 1) {
									return splittedSubContainerMessage[0].replace("||", "");
								}
							}
						}
					}
				}
			}
		}
		return containerID;
	}

	private void buildMsgType(StringBuilder sb, boolean isBSM) {
		if (isBSM) {
			sb.append(MessageBuilderConstants.START_BSM.getMessage());
		} else {
			sb.append(MessageBuilderConstants.START_BPM.getMessage());
		}
		sb.append(System.lineSeparator());
	}

	private void buildVElement(PaxItinerary paxItinerary, StringBuilder sb, String airport, 
			String bagActionType) {
		String[] splitSegments = splitSegments(paxItinerary);
		String[] firstSplitSegment = splitSegments[0].split("-");
		if (bagActionType != null && bagActionType.equalsIgnoreCase(MessageBuilderConstants.LOCAL_BAG.getMessage())) {
			sb.append(MessageBuilderConstants.DOT_V.getMessage()).append(MessageBuilderConstants.LOCAL_BAG.getMessage())
					.append(airport).append(System.lineSeparator());
		}
		else if (bagActionType != null && bagActionType.equalsIgnoreCase(MessageBuilderConstants.TRANSFER_BAG.getMessage())) {
			sb.append(MessageBuilderConstants.DOT_V.getMessage()).append(MessageBuilderConstants.TRANSFER_BAG.getMessage())
					.append(airport).append(System.lineSeparator());
		}
		else if (bagActionType != null && bagActionType.equalsIgnoreCase(MessageBuilderConstants.TERMINATE_BAG.getMessage())) {
			sb.append(MessageBuilderConstants.DOT_V.getMessage()).append(MessageBuilderConstants.TERMINATE_BAG.getMessage())
					.append(airport).append(System.lineSeparator());
		}
	}

	private void buildOnlyCurrentSegment(PaxItinerary paxItinerary, StringBuilder sb) {
		sb.append(MessageBuilderConstants.DOT_F.getMessage());
		String[] splitSegments = splitSegments(paxItinerary);
		String[] segment = splitSegments[0].split("-");
		buildSegment(sb, segment, null);
	}
	
	private void buildOnlyOnwardSegments(String[] splitSegments, StringBuilder bsm, 
			int index) {
		for (int i = index; i < splitSegments.length; i++) {
			bsm.append(MessageBuilderConstants.DOT_O.getMessage());
			String[] segment = splitSegments[i].split("-");
			buildSegment(bsm, segment, null);
		}
	}

	private void buildOnlyOnwardSegments(PaxItinerary paxItinerary, StringBuilder sb) {
		String[] splitSegments = splitSegments(paxItinerary);
		for (int i = 1; i < splitSegments.length; i++) {
			sb.append(MessageBuilderConstants.DOT_O.getMessage());
			String[] segment = splitSegments[i].split("-");
			buildSegment(sb, segment, null);
		}
	}

	private String[] splitSegments(PaxItinerary paxItinerary) {
		String[] splitSegments = paxItinerary.getFlight().split(MessageBuilderConstants.HASH.getMessage());
		return splitSegments;
	}

	private void buildSegment(StringBuilder sb, String[] segment, String element) {
		if (MessageBuilderConstants.ELEMENT_I.getMessage().equalsIgnoreCase(element)) {
			sb.append(segment[0]).append(MessageBuilderConstants.FWD_SLASH.getMessage()).append(segment[1].substring(0, 5))
			.append(MessageBuilderConstants.FWD_SLASH.getMessage()).append(segment[2])
			.append(MessageBuilderConstants.FWD_SLASH.getMessage())
			.append(MessageBuilderConstants.CABIN_CLASS_Y.getMessage()).append(System.lineSeparator());
		}
		else {
			sb.append(segment[0]).append(MessageBuilderConstants.FWD_SLASH.getMessage()).append(segment[1].substring(0, 5))
			.append(MessageBuilderConstants.FWD_SLASH.getMessage()).append(segment[3])
			.append(MessageBuilderConstants.FWD_SLASH.getMessage())
			.append(MessageBuilderConstants.CABIN_CLASS_Y.getMessage()).append(System.lineSeparator());
		}
		
	}

	private void buildNElement(PaxItinerary paxItinerary, StringBuilder sb, String messageType) {
		int firstTag = generateAndSetBagTagsToPaxItineraryIfNotExist(paxItinerary, messageType);
		if (firstTag == 0 && messageType.equalsIgnoreCase(MessageBuilderConstants.BPM_MESSAGE_TYPE.getMessage())) {
			sb.append(MessageBuilderConstants.DOT_N.getMessage())
					.append(paxItinerary.getListOfGeneratedBagTags().get(0)).append("00")
					.append(new Integer(1)).append(System.lineSeparator());
		}
		else {
			sb.append(MessageBuilderConstants.DOT_N.getMessage()).append(firstTag).append("00")
				.append(paxItinerary.getNumberOfCheckedInBags()).append(System.lineSeparator());
		}
	}

	private int generateAndSetBagTagsToPaxItineraryIfNotExist(PaxItinerary paxItinerary, String messageType) {
		int firstTag = 0;
		if (CollectionUtils.isEmpty(paxItinerary.getListOfGeneratedBagTags())) {
			List<String> generatedBagTags = new ArrayList<String>();
			Random r = new Random();
			//firstTag = r.nextInt((100000 - 1000) + 1);
			firstTag = r.nextInt(100000 - 1000) + 999999999;
			int secondTag;
			generatedBagTags.add(String.valueOf(firstTag));
			if (paxItinerary.getNumberOfCheckedInBags() != null && paxItinerary.getNumberOfCheckedInBags() > 1) {
				secondTag = firstTag + 1;
				generatedBagTags.add(String.valueOf(secondTag));
			}
			paxItinerary.setListOfGeneratedBagTags(generatedBagTags);
		}
		else if (messageType.equalsIgnoreCase(MessageBuilderConstants.BSM_MESSAGE_TYPE.getMessage())) {
			//just setting the get existing bag tag
			firstTag = Integer.parseInt(paxItinerary.getListOfGeneratedBagTags().get(0));
		}
		return firstTag;
	}

	private void buildPElement(PaxItinerary paxItinerary, StringBuilder bsm) {
		bsm.append(MessageBuilderConstants.DOT_P.getMessage()).append(paxItinerary.getFirstName())
				.append(MessageBuilderConstants.FWD_SLASH.getMessage()).append(paxItinerary.getLastName())
				.append(System.lineSeparator());
	}

	private void buildEndMsg(StringBuilder msg, boolean isBSM) {
		if (isBSM) {
			msg.append(MessageBuilderConstants.END_BSM.getMessage());
		} else {
			msg.append(MessageBuilderConstants.END_BPM.getMessage());
		}

	}

	public String buildBPMForSorting(LoadUnloadBag loadUnloadBag, BagEvent bagEvent) throws InvalidBagTagException {
		StringBuilder bpm = new StringBuilder();
		buildMsgType(bpm, false);
		List<PaxItinerary> paxItineraries = bookingDAO.getBookings(loadUnloadBag.getDateOfTravel());
		PaxItinerary validPaxItinerary = validateBagTagAndFilterItineraries(loadUnloadBag, paxItineraries);
		if (validPaxItinerary != null) {
			String date = validPaxItinerary.getDateOfTravel().substring(0, 2);
			String month = validPaxItinerary.getDateOfTravel().substring(2, 5);
			String year = validPaxItinerary.getDateOfTravel().substring(5);
			bagEvent.setBagDate(BagTrackUtil.monthInNumber(month) + "/" +  date + "/" + year);
			bagEvent.setFrom(validPaxItinerary.getLoggedInAgent());
			bagEvent.setFlight(validPaxItinerary.getFlight());
		}
		String bagType = getBagType(validPaxItinerary, loadUnloadBag.getAgentLocation());
		buildVElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation(),
				bagType);
		//build .J element
		buildJElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation(), MessageBuilderConstants.SECONDARY_CODE_S.getMessage(), 
				MessageBuilderConstants.SCREENING_DEVICE_1.getMessage(), MessageBuilderConstants.READING_LOCATION_1.getMessage(), bagType);
		//buildOnlyCurrentSegment(validPaxItinerary, bpm);
		buildFElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation(), bagType);
		//buildUElement(loadUnloadBag, bpm); // ULD Container
		buildNElement(validPaxItinerary, bpm, MessageBuilderConstants.BPM_MESSAGE_TYPE.getMessage());
		buildOnlyInboundSegment(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation());
		//buildOnlyOnwardSegments(validPaxItinerary, bpm);
		buildOElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation());
		buildSElement(validPaxItinerary, bpm, MessageBuilderConstants.BOARDED.getMessage());
		buildPElement(validPaxItinerary, bpm);
		buildEndMsg(bpm, false);
		return bpm.toString();
	}

	public String buildBPMForScreening(LoadUnloadBag loadUnloadBag, BagEvent bagEvent) throws InvalidBagTagException {
		StringBuilder bpm = new StringBuilder();
		buildMsgType(bpm, false);
		List<PaxItinerary> paxItineraries = bookingDAO.getBookings(loadUnloadBag.getDateOfTravel());
		PaxItinerary validPaxItinerary = validateBagTagAndFilterItineraries(loadUnloadBag, paxItineraries);
		if (validPaxItinerary != null) {
			String date = validPaxItinerary.getDateOfTravel().substring(0, 2);
			String month = validPaxItinerary.getDateOfTravel().substring(2, 5);
			String year = validPaxItinerary.getDateOfTravel().substring(5);
			bagEvent.setBagDate(BagTrackUtil.monthInNumber(month) + "/" +  date + "/" + year);
			bagEvent.setFrom(validPaxItinerary.getLoggedInAgent());
			bagEvent.setFlight(validPaxItinerary.getFlight());
		}
		String bagType = getBagType(validPaxItinerary, loadUnloadBag.getAgentLocation());
		buildVElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation(),
				bagType);
		//build .J element
		buildJElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation(), MessageBuilderConstants.SECONDARY_CODE_S.getMessage(), 
				MessageBuilderConstants.SCREENING_DEVICE_2.getMessage(), MessageBuilderConstants.READING_LOCATION_2.getMessage(), bagType);
		//buildOnlyCurrentSegment(validPaxItinerary, bpm);
		buildFElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation(), bagType);
		//buildUElement(loadUnloadBag, bpm); // ULD Container
		buildNElement(validPaxItinerary, bpm, MessageBuilderConstants.BPM_MESSAGE_TYPE.getMessage());
		buildOnlyInboundSegment(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation());
		//buildOnlyOnwardSegments(validPaxItinerary, bpm);
		buildOElement(validPaxItinerary, bpm, loadUnloadBag.getAgentLocation());
		buildSElement(validPaxItinerary, bpm, MessageBuilderConstants.BOARDED.getMessage());
		buildPElement(validPaxItinerary, bpm);
		buildXElement(validPaxItinerary, bpm);
		buildEndMsg(bpm, false);
		return bpm.toString();
	}

	private String getBagType(PaxItinerary validPaxItinerary, String agentLocation) {
		String[] splitSegments = splitSegments(validPaxItinerary);
		String segment = splitSegments[splitSegments.length - 1];
		String[] splitFlights = splitFlights(segment, MessageBuilderConstants.HYPHEN.getMessage());
		if (splitFlights[3].equalsIgnoreCase(agentLocation)) {
			return MessageBuilderConstants.TERMINATE_BAG.getMessage();
		}
		else {
			return MessageBuilderConstants.TRANSFER_BAG.getMessage();
		}
	}

	private void buildXElement(PaxItinerary validPaxItinerary, StringBuilder bpm) {
		bpm.append(MessageBuilderConstants.DOT_X.getMessage()).append("/CLR//XRAY//").append(System.lineSeparator());
	}

}
