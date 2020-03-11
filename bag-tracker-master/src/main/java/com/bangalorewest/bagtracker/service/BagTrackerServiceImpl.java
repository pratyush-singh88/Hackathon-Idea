/**
 * 
 */
package com.bangalorewest.bagtracker.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.bangalorewest.bagtracker.constants.MessageBuilderConstants;
import com.bangalorewest.bagtracker.dto.BPMStatusResponse;
import com.bangalorewest.bagtracker.dto.BagEvent;
import com.bangalorewest.bagtracker.dto.BagHistoriesFromBlockchain;
import com.bangalorewest.bagtracker.dto.BagHistoryDTO;
import com.bangalorewest.bagtracker.dto.BagHistoryFromBlockchain;
import com.bangalorewest.bagtracker.dto.BagStatusResponse;
import com.bangalorewest.bagtracker.dto.ConfirmLoginStatusDTO;
import com.bangalorewest.bagtracker.dto.ConfirmMessageGeneration;
import com.bangalorewest.bagtracker.dto.ConfirmSave;
import com.bangalorewest.bagtracker.dto.LoadUnloadBag;
import com.bangalorewest.bagtracker.dto.LoginDTO;
import com.bangalorewest.bagtracker.dto.PaxItinerary;
import com.bangalorewest.bagtracker.exception.AuthenticationFailureException;
import com.bangalorewest.bagtracker.exception.BagTagUpdateFailedException;
import com.bangalorewest.bagtracker.exception.InvalidBagTagException;
import com.bangalorewest.bagtracker.repository.BagAgentRepository;
import com.bangalorewest.bagtracker.repository.BookingRepository;
import com.bangalorewest.bagtracker.util.BagTrackUtil;
import com.bangalorewest.bagtracker.util.ConfigReader;
import com.bangalorewest.bagtracker.util.DateTimeUtil;
import com.bangalorewest.bagtracker.util.LoginHelper;

/**
 * @author sudhanshu.singh
 *
 */
@Service
public class BagTrackerServiceImpl implements BagTrackerService {

	@Autowired
	BagAgentRepository bagAgentRepository;

	@Autowired
	MessageBuilder msgBuilder;

	@Autowired
	BookingRepository bookingRepository;

	@Autowired
	RestTemplate restTemplate;

	@Override
	public BagStatusResponse fetchLatestBagStatus(String bagTagID, String dateOfTravel) 
			throws InvalidBagTagException {
		// TODO Auto-generated method stub
		String date = dateOfTravel.substring(0, 2);
		String month = dateOfTravel.substring(2, 5);
		String year = dateOfTravel.substring(5);
		HttpHeaders headers = issueSecurityTokenAndSetHeaders();
		String URL = ConfigReader.getBaseUrl().concat(ConfigReader.getStatusApiUrl()).concat(bagTagID).concat("&")
				.concat("messageDate=").concat(BagTrackUtil.monthInNumber(month) + "/" +  date + "/" + year);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		ResponseEntity<BagHistoriesFromBlockchain> resp = restTemplate.exchange(URL, HttpMethod.GET, entity,
				BagHistoriesFromBlockchain.class);
		BagStatusResponse bagStatus = createResponse(resp.getBody(), bagTagID, dateOfTravel);
		return bagStatus;
	}

	private BagStatusResponse createResponse(BagHistoriesFromBlockchain bagHistoriesFromBlockchain, 
			String bagTagID, String dateOfTravel) throws InvalidBagTagException {
		// TODO Auto-generated method stub
		if (bagHistoriesFromBlockchain.getData() == null || bagHistoriesFromBlockchain.getData().isEmpty()) {
			throw new InvalidBagTagException("Invalid BagTag Passed to get status");
		}
		
		PaxItinerary validPaxItinerary = msgBuilder.getValidPaxItinerary(bagTagID, dateOfTravel);
		
		BagStatusResponse response = new BagStatusResponse();
		BagHistoryFromBlockchain bagHistoryFromBlockchain = bagHistoriesFromBlockchain.getData().get(0);
		response.setAirportCode(bagHistoryFromBlockchain.getAirportCode());
		response.setDate(bagHistoryFromBlockchain.getMessageDate());
		response.setStatus(findBagStatus(bagHistoryFromBlockchain));
		response.setFlight(findFlight(bagHistoryFromBlockchain));
		BPMStatusResponse bPMStatusResponse = bpmStatusResponse(bagHistoryFromBlockchain.getMessage(), 
				response);
		response.setBPMStatusResponse(bPMStatusResponse );
		if  (validPaxItinerary != null) {
			response.setFirstName(validPaxItinerary.getFirstName());
			response.setLastName(validPaxItinerary.getLastName());
		}
		return response;
	}

	private BPMStatusResponse bpmStatusResponse(String message, BagStatusResponse response) {
		if (message != null && message.contains(".U/")) {
//			String[] elementArray = message.split(".U/");
//			if (elementArray[1] != null) {
//				BPMStatusResponse bpmStatusResponse = new BPMStatusResponse();
//				String[] splittedElement = elementArray[1].split("||");
//				bpmStatusResponse.setContainerID(splittedElement[0]);
//				return bpmStatusResponse;
//			}
			BPMStatusResponse bpmStatusResponse = new BPMStatusResponse();
			bpmStatusResponse.setContainerID("AKE12345AI-" 
					+ response.getFlight() + "-" + response.getDate());
			return bpmStatusResponse;
		}
		return null;
	}

	private String findFlight(BagHistoryFromBlockchain bagHistoryFromBlockchain) {
		// TODO Auto-generated method stub
		String message = bagHistoryFromBlockchain.getMessage();
		if (message != null) {
			String[] messageElement = message.split(".F/");
			String dotFElement = messageElement[1];
			if (dotFElement != null) {
				String[] dotFSplittedElement = dotFElement.split("/");
				return dotFSplittedElement[0];
			}
		}
		return null;
	}

	private String findBagStatus(BagHistoryFromBlockchain bagHistoryFromBlockchain) {
		StringBuilder localBag = new StringBuilder();
		localBag.append(".V/1L");
		
		StringBuilder transferBag = new StringBuilder();
		transferBag.append(".V/1T");
		
		StringBuilder terminateBag = new StringBuilder();
		terminateBag.append(".V/1X");
		
		if (bagHistoryFromBlockchain.getMessage().contains(MessageBuilderConstants.START_BSM.getMessage())
				&& bagHistoryFromBlockchain.getMessage().contains(localBag)) {
			return "BAG CHECKEDIN";
		}
		else if (bagHistoryFromBlockchain.getMessage().contains(MessageBuilderConstants.START_BSM.getMessage())
				&& bagHistoryFromBlockchain.getMessage().contains(transferBag)) {
			return "BAG EXPECTED";
		}
		else if (bagHistoryFromBlockchain.getMessage().contains(MessageBuilderConstants.START_BSM.getMessage())
				&& bagHistoryFromBlockchain.getMessage().contains(terminateBag)) {
			return "BAG EXPECTED";
		}
		else if (bagHistoryFromBlockchain.getMessage().contains(MessageBuilderConstants.DOT_U.getMessage())
				&& bagHistoryFromBlockchain.getMessage()
						.contains(MessageBuilderConstants.DOT_B.getMessage() + "OFF")) {
			return "BAG OFFLOADED";
		} 
//		else if (bagHistoryFromBlockchain.getMessage()
//				.contains(MessageBuilderConstants.DOT_B.getMessage() + "OFF")) {
//			return "BAG OFFLOADED";
//		}
		else if (bagHistoryFromBlockchain.getMessage().contains(MessageBuilderConstants.DOT_U.getMessage())) {
			return "BAG LOADED ONTO CONTAINER";
		} 
		else if (bagHistoryFromBlockchain.getMessage().contains("HLD")) {
			return "BAG LOADED ONTO AIRCRAFT";
		}
		else if (bagHistoryFromBlockchain.getMessage().contains(MessageBuilderConstants.DOT_X.getMessage())) {
			return "BAG SCREENED";
		}
		else if (bagHistoryFromBlockchain.getMessage().contains(MessageBuilderConstants.DOT_J.getMessage())) {
			return "BAG SORTED";
		} 
		return null;
	}

	@Override
	public Map<String, List<BagHistoryDTO>> fetchBagHistory(String bagTagID, String dateOfTravel) 
			throws InvalidBagTagException {
		String date = dateOfTravel.substring(0, 2);
		String month = dateOfTravel.substring(2, 5);
		String year = dateOfTravel.substring(5);
		HttpHeaders headers = issueSecurityTokenAndSetHeaders();
		String URL = ConfigReader.getBaseUrl().concat(ConfigReader.getHistoryApiUrl()).concat(bagTagID).concat("&")
				.concat("messageDate=").concat(BagTrackUtil.monthInNumber(month) + "/" +  date + "/" + year);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		ResponseEntity<BagHistoriesFromBlockchain> resp = restTemplate.exchange(URL, HttpMethod.GET, entity,
				BagHistoriesFromBlockchain.class);
		Map<String, List<BagHistoryDTO>> bagHistories = createHistoryResponse(resp.getBody(), bagTagID, dateOfTravel);
		return bagHistories;
	}

	private Map<String, List<BagHistoryDTO>> createHistoryResponse(BagHistoriesFromBlockchain bagHistoriesFromBlockchain, 
			String bagTagID, String dateOfTravel) 
			throws InvalidBagTagException {
		Map<String, List<BagHistoryDTO>> bagHistoryDTOMap = null;
		List<BagHistoryDTO> bagHistoryDTOsList = new ArrayList<BagHistoryDTO>();
		if (bagHistoriesFromBlockchain.getData() == null || bagHistoriesFromBlockchain.getData().isEmpty()) {
			throw new InvalidBagTagException("Invalid BagTag Passed to get history");
		}
		
		PaxItinerary validPaxItinerary = msgBuilder.getValidPaxItinerary(bagTagID, dateOfTravel);
		List<BagHistoryFromBlockchain> data = bagHistoriesFromBlockchain.getData();
		if (data != null && !data.isEmpty()) {
			for (BagHistoryFromBlockchain bagHistoryFromBlockchain : data) {
				BagHistoryDTO bagHistoryDTO = new BagHistoryDTO();
				bagHistoryDTO.setStatus(findBagStatus(bagHistoryFromBlockchain));
				bagHistoryDTO.setTimestamp(bagHistoryFromBlockchain.getTimestamp());
				bagHistoryDTO.setFrom(bagHistoryFromBlockchain.getFrom());
				bagHistoryDTO.setDepartureFlight(departureFlight(bagHistoryFromBlockchain.getMessage()));
				bagHistoryDTO.setLoadFlight(loadFlight((bagHistoryFromBlockchain.getMessage())));
				bagHistoryDTO.setBagTagID(bagHistoryFromBlockchain.getBagTagID());
				bagHistoryDTO.setAirportCode(bagHistoryFromBlockchain.getAirportCode());
				
				if (validPaxItinerary != null) {
					bagHistoryDTO.setFirstName(validPaxItinerary.getFirstName());
					bagHistoryDTO.setLastName(validPaxItinerary.getLastName());
					bagHistoryDTO.setNumberOfCheckedInBags(validPaxItinerary.getNumberOfCheckedInBags());
				}
				
				bagHistoryDTOsList.add(bagHistoryDTO);
			}
			Collections.reverse(bagHistoryDTOsList);
			
			if (!bagHistoryDTOsList.isEmpty()) {
				bagHistoryDTOMap = new HashMap<String, List<BagHistoryDTO>>();
				for (BagHistoryDTO bagHistoryDTO : bagHistoryDTOsList) {
					if (bagHistoryDTOMap.isEmpty()) {
						List<BagHistoryDTO> bagHistoryDTOList = new ArrayList<BagHistoryDTO>();
						bagHistoryDTOList.add(bagHistoryDTO);
						bagHistoryDTOMap.put(bagHistoryDTO.getAirportCode(), bagHistoryDTOList);
					}
					else {
						if (bagHistoryDTOMap.containsKey(bagHistoryDTO.getAirportCode())) {
							bagHistoryDTOMap.get(bagHistoryDTO.getAirportCode()).add(bagHistoryDTO);
						}
						else {
							List<BagHistoryDTO> bagHistoryDTOList = new ArrayList<BagHistoryDTO>();
							bagHistoryDTOList.add(bagHistoryDTO);
							bagHistoryDTOMap.put(bagHistoryDTO.getAirportCode(), bagHistoryDTOList);
						}
					}
				}
			}
		}
		
		return bagHistoryDTOMap;
	}

	private String loadFlight(String message) {
		if (message != null && !message.isEmpty() && message.contains(MessageBuilderConstants.DOT_U.getMessage())) {
			return departureFlight(message);
		}
		return null;
	}

	private String departureFlight(String message) {
		if (message != null && !message.isEmpty()) {
			String[] splittedForDepartureStation = message.split(".V/1");
			if (splittedForDepartureStation != null && splittedForDepartureStation.length >= 2) {
				String[] departureStationArray = splittedForDepartureStation[1].split(".F/");
				if (departureStationArray != null && departureStationArray.length >= 2) {
					String[] flightSplittedArray = departureStationArray[1].split(".N/");
					if (flightSplittedArray != null && flightSplittedArray.length >= 1) {
						String[] flightSplit = flightSplittedArray[0].split("/");
						return flightSplit[0] + "/" + flightSplit[1] + "/" + departureStationArray[0].substring(1, 4);
					}
				}
			}
		}
		return null;
	}

	@Override
	public ConfirmMessageGeneration generateBSM(PaxItinerary paxItinerary) throws BagTagUpdateFailedException {
		List<String> bsmMessages = null;
		
		List<String> allAirportList = getAllAirportList(paxItinerary);
		if (allAirportList != null && !allAirportList.isEmpty()) {
			bsmMessages = new ArrayList<String>();
			String URL = ConfigReader.getBaseUrl().concat(ConfigReader.getPostBsm());
			HttpHeaders headers = issueSecurityTokenAndSetHeaders();
			
			for (int i = 0; i < allAirportList.size(); i++) {
				String bsmMessage = null;
				//BSM : Local Bag
				if (i == 0) {
					bsmMessage = msgBuilder.buildBSM(paxItinerary, allAirportList.get(i), 
							MessageBuilderConstants.LOCAL_BAG.getMessage(), i);
					bsmMessages.add(bsmMessage);
				}
				//BSM : Terminate Bag
				else if (i+1 == allAirportList.size()) {
					bsmMessage = msgBuilder.buildBSM(paxItinerary, allAirportList.get(i), 
							MessageBuilderConstants.TERMINATE_BAG.getMessage(), i);
					bsmMessages.add(bsmMessage);
				}
				//BSM : Transfer Bag 
				else {
					bsmMessage = msgBuilder.buildBSM(paxItinerary, allAirportList.get(i), 
							MessageBuilderConstants.TRANSFER_BAG.getMessage(), i);
					bsmMessages.add(bsmMessage);
				}
				if (paxItinerary.getNumberOfCheckedInBags() >= 1) {
					for (String bagTagID : paxItinerary.getListOfGeneratedBagTags()) {
						BagEvent bagEvent = new BagEvent();
						bagEvent.setBagTagID(bagTagID);
						String date = paxItinerary.getDateOfTravel().substring(0, 2);
						String month = paxItinerary.getDateOfTravel().substring(2, 5);
						String year = paxItinerary.getDateOfTravel().substring(5);
						bagEvent.setBagDate(BagTrackUtil.monthInNumber(month) + "/" +  date + "/" + year);
						//bagEvent.setBagDate(paxItinerary.getDateOfTravel());
						bagEvent.setFrom(paxItinerary.getLoggedInAgent());
						bagEvent.setMessage(bsmMessage.replaceAll(System.lineSeparator(), "||"));
						// persist each bagEvent to Blockchain

						String requestJSON = "{\"bagTagID\" : \"" + bagEvent.getBagTagID() + "\", \"from\" : \""
								+ paxItinerary.getLoggedInAgent() + "\", \"messageDate\" : \"" + bagEvent.getBagDate()
								+ "\", \"message\" : \"" + bagEvent.getMessage() + "\", \"to\" : \"" + "BAG AGENT" 
								+ "\", \"airportCode\" : \"" + BagTrackUtil.airportCodeFromMessage(bagEvent.getMessage()) + "\", "
										+ "\"timestamp\":\"" + new Date() + "\"}";
						System.out.println(requestJSON);
						HttpEntity<String> request = new HttpEntity<String>(requestJSON, headers);
						ResponseEntity<ConfirmSave> postForEntity = restTemplate.postForEntity(URL, request, ConfirmSave.class);
					}
				}
			}
		}
		
		updateGeneratedBagTagsToDB(paxItinerary);

		ConfirmMessageGeneration confirmMsg = new ConfirmMessageGeneration();
		confirmMsg.setMessage(bsmMessages);
		confirmMsg.setFrom(paxItinerary.getLoggedInAgent());
		return confirmMsg;

	}
	
	
	private List<String> getAllAirportList(PaxItinerary paxItinerary) {
		List<String> allAirportList = new ArrayList<String>();
		String[] splitSegments = splitSegments(paxItinerary, MessageBuilderConstants.HASH.getMessage());
		for (String segment : splitSegments) {
			String[] splitFlights = splitFlights(segment, MessageBuilderConstants.HYPHEN.getMessage());
			for (String flight : splitFlights) {
				if (splitFlights[2].equalsIgnoreCase(flight) || splitFlights[3].equalsIgnoreCase(flight)) {
					if (allAirportList.isEmpty()) {
						allAirportList.add(flight);
					}
					else {
						if (!allAirportList.contains(flight)) {
							allAirportList.add(flight);
						}
					}
				}
			}
		}
		return allAirportList;
	}
	
	private String[] splitFlights(String segment, String splitter) {
		String[] splitFlights = segment.split(splitter);
		return splitFlights;
	}

	private String[] splitSegments(PaxItinerary paxItinerary, String splitter) {
		String[] splitSegments = paxItinerary.getFlight().split(splitter);
		return splitSegments;
	}

	private HttpHeaders issueSecurityTokenAndSetHeaders() {
		String bearerToken = LoginHelper.issueBlockChainSecurityToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + bearerToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	@Override
	public ConfirmMessageGeneration generateLoadBPM(LoadUnloadBag loadUnloadBag) throws InvalidBagTagException {
		List<String> bpmMessages = null;
		BagEvent bagEvent = new BagEvent();
		String bpmMessage = msgBuilder.buildBPMForLoad(loadUnloadBag, bagEvent);
		
		if (!bpmMessage.isEmpty()) {
			bpmMessages = new ArrayList<String>();
			bpmMessages.add(bpmMessage);
			String URL = ConfigReader.getBaseUrl().concat(ConfigReader.getPostBsm());
			HttpHeaders headers = issueSecurityTokenAndSetHeaders();
			
			bagEvent.setBagTagID(loadUnloadBag.getBagTagID());
			bagEvent.setMessage(bpmMessage.replaceAll(System.lineSeparator(), "||"));
			// persist each bagEvent to Blockchain

			String requestJSON = "{\"bagTagID\" : \"" + bagEvent.getBagTagID() + "\", \"from\" : \""
					+ loadUnloadBag.getLoggedInAgent() + "\", \"messageDate\" : \"" + bagEvent.getBagDate()
					+ "\", \"message\" : \"" + bagEvent.getMessage() + "\", \"to\" : \"" + "BAG AGENT" 
					+ "\", \"airportCode\" : \"" + loadUnloadBag.getAgentLocation() + "\", "
							+ "\"timestamp\":\"" + new Date() + "\"}";
			System.out.println(requestJSON);
			HttpEntity<String> request = new HttpEntity<String>(requestJSON, headers);
			ResponseEntity<ConfirmSave> postForEntity = restTemplate.postForEntity(URL, request, ConfirmSave.class);
		}
		ConfirmMessageGeneration confirmMsg = new ConfirmMessageGeneration();
		confirmMsg.setMessage(bpmMessages);
		confirmMsg.setFrom(loadUnloadBag.getLoggedInAgent());

		// Call blockchain - persist

		return confirmMsg;
	}

	@Override
	public ConfirmMessageGeneration generateUnloadBPM(LoadUnloadBag loadUnloadRequest) throws InvalidBagTagException {
		// TODO Auto-generated method stub
		List<String> bpmMessages = null;
		BagEvent bagEvent = new BagEvent();
		String bpmMessage = msgBuilder.buildBPMForUnload(loadUnloadRequest, bagEvent);
		
		if (!bpmMessage.isEmpty()) {
			bpmMessages = new ArrayList<String>();
			bpmMessages.add(bpmMessage);
			String URL = ConfigReader.getBaseUrl().concat(ConfigReader.getPostBsm());
			HttpHeaders headers = issueSecurityTokenAndSetHeaders();
			
			//BagEvent bagEvent = new BagEvent();
			bagEvent.setBagTagID(loadUnloadRequest.getBagTagID());
			//bagEvent.setBagDate(paxItinerary.getDateOfTravel());
			//bagEvent.setFrom(paxItinerary.getLoggedInAgent());
			bagEvent.setMessage(bpmMessage.replaceAll(System.lineSeparator(), "||"));
			// persist each bagEvent to Blockchain

			String requestJSON = "{\"bagTagID\" : \"" + bagEvent.getBagTagID() + "\", \"from\" : \""
					+ loadUnloadRequest.getLoggedInAgent() + "\", \"messageDate\" : \"" + bagEvent.getBagDate()
					+ "\", \"message\" : \"" + bagEvent.getMessage() + "\", \"to\" : \"" + "BAG AGENT" 
					+ "\", \"airportCode\" : \"" + loadUnloadRequest.getAgentLocation() + "\", "
							+ "\"timestamp\":\"" + new Date() + "\"}";
			System.out.println(requestJSON);
			HttpEntity<String> request = new HttpEntity<String>(requestJSON, headers);
			ResponseEntity<ConfirmSave> postForEntity = restTemplate.postForEntity(URL, request, ConfirmSave.class);
		}
		
		ConfirmMessageGeneration confirmMsg = new ConfirmMessageGeneration();
		confirmMsg.setMessage(bpmMessages);
		confirmMsg.setFrom(loadUnloadRequest.getLoggedInAgent());

		return confirmMsg;
	}

	private void updateGeneratedBagTagsToDB(PaxItinerary paxItinerary) throws BagTagUpdateFailedException {
		String commaSeparatedBagTags = String.join(",", paxItinerary.getListOfGeneratedBagTags());
		Integer numberOfUpdatedRecords = bookingRepository.updateGeneratedBagTagsToBookings(commaSeparatedBagTags,
				paxItinerary.getEmailID(), paxItinerary.getFirstName(), 
				DateTimeUtil.parseStringDate(paxItinerary.getDateOfTravel()), paxItinerary.getNumberOfCheckedInBags());
		if (numberOfUpdatedRecords == null || numberOfUpdatedRecords == 0) {
			throw new BagTagUpdateFailedException("Couldn't update bagtags to DB");
		}
	}

	@Override
	public ConfirmLoginStatusDTO checkAndLogin(LoginDTO login) throws AuthenticationFailureException {
		List<Object> authenticatedUserDetails = null;
		if (Strings.isEmpty(login.getUserRole())) {
			try {
				authenticatedUserDetails = (List<Object>) bagAgentRepository.checkLoginWithoutRole(login.getUserPassword(),
						login.getUserName());
			} catch (Exception e) {
				throw new AuthenticationFailureException("Login Failed");
			}
		} 
		else if (Strings.isEmpty(login.getAgentLocation())) {
			try {
				authenticatedUserDetails = (List<Object>) bagAgentRepository.checkLogin(login.getUserPassword(),
						login.getUserName(), login.getUserRole());
			} catch (Exception e) {
				throw new AuthenticationFailureException("Login Failed");
			}
		} else {
			try {
				authenticatedUserDetails = (List<Object>) bagAgentRepository.checkLoginForLoadUnload(
						login.getUserPassword(), login.getUserName(), login.getUserRole(), login.getAgentLocation());
			} catch (Exception e) {
				throw new AuthenticationFailureException("Ground Handler login failed");
			}
		}
		if (!CollectionUtils.isEmpty(authenticatedUserDetails)) {
			return mapRetrievedDataToDTO(authenticatedUserDetails);
		} else {
			throw new AuthenticationFailureException("Agent Not Authorized");
		}
	}

	private ConfirmLoginStatusDTO mapRetrievedDataToDTO(List<Object> authenticatedUserDetails) {
		ConfirmLoginStatusDTO confirmLogin = null;
		Iterator itr = authenticatedUserDetails.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			String userName = String.valueOf(obj[0]);
			String userRole = String.valueOf(obj[1]);
			String agentLocation = String.valueOf(obj[2]);
			confirmLogin = new ConfirmLoginStatusDTO(userName, userRole, "Authentication Successful", agentLocation);
		}
		return confirmLogin;

	}

	@Override
	public ConfirmMessageGeneration generateSortingBPM(LoadUnloadBag loadUnloadBag) throws InvalidBagTagException {
		List<String> bpmMessages = null;
		BagEvent bagEvent = new BagEvent();
		String bpmMessage = msgBuilder.buildBPMForSorting(loadUnloadBag, bagEvent);
		
		if (!bpmMessage.isEmpty()) {
			bpmMessages = new ArrayList<String>();
			bpmMessages.add(bpmMessage);
			String URL = ConfigReader.getBaseUrl().concat(ConfigReader.getPostBsm());
			HttpHeaders headers = issueSecurityTokenAndSetHeaders();
			
			bagEvent.setBagTagID(loadUnloadBag.getBagTagID());
			bagEvent.setMessage(bpmMessage.replaceAll(System.lineSeparator(), "||"));
			// persist each bagEvent to Blockchain

			String requestJSON = "{\"bagTagID\" : \"" + bagEvent.getBagTagID() + "\", \"from\" : \""
					+ loadUnloadBag.getLoggedInAgent() + "\", \"messageDate\" : \"" + bagEvent.getBagDate()
					+ "\", \"message\" : \"" + bagEvent.getMessage() + "\", \"to\" : \"" + "BAG AGENT" 
					+ "\", \"airportCode\" : \"" + loadUnloadBag.getAgentLocation() + "\", "
							+ "\"timestamp\":\"" + new Date() + "\"}";
			System.out.println(requestJSON);
			HttpEntity<String> request = new HttpEntity<String>(requestJSON, headers);
			ResponseEntity<ConfirmSave> postForEntity = restTemplate.postForEntity(URL, request, ConfirmSave.class);
		}
		ConfirmMessageGeneration confirmMsg = new ConfirmMessageGeneration();
		confirmMsg.setMessage(bpmMessages);
		confirmMsg.setFrom(loadUnloadBag.getLoggedInAgent());

		// Call blockchain - persist

		return confirmMsg;
	}

	@Override
	public ConfirmMessageGeneration generateScreeningBPM(LoadUnloadBag loadUnloadBag) throws InvalidBagTagException {
		List<String> bpmMessages = null;
		BagEvent bagEvent = new BagEvent();
		String bpmMessage = msgBuilder.buildBPMForScreening(loadUnloadBag, bagEvent);
		
		if (!bpmMessage.isEmpty()) {
			bpmMessages = new ArrayList<String>();
			bpmMessages.add(bpmMessage);
			String URL = ConfigReader.getBaseUrl().concat(ConfigReader.getPostBsm());
			HttpHeaders headers = issueSecurityTokenAndSetHeaders();
			
			bagEvent.setBagTagID(loadUnloadBag.getBagTagID());
			bagEvent.setMessage(bpmMessage.replaceAll(System.lineSeparator(), "||"));
			// persist each bagEvent to Blockchain

			String requestJSON = "{\"bagTagID\" : \"" + bagEvent.getBagTagID() + "\", \"from\" : \""
					+ loadUnloadBag.getLoggedInAgent() + "\", \"messageDate\" : \"" + bagEvent.getBagDate()
					+ "\", \"message\" : \"" + bagEvent.getMessage() + "\", \"to\" : \"" + "BAG AGENT" 
					+ "\", \"airportCode\" : \"" + loadUnloadBag.getAgentLocation() + "\", "
							+ "\"timestamp\":\"" + new Date() + "\"}";
			System.out.println(requestJSON);
			HttpEntity<String> request = new HttpEntity<String>(requestJSON, headers);
			ResponseEntity<ConfirmSave> postForEntity = restTemplate.postForEntity(URL, request, ConfirmSave.class);
		}
		ConfirmMessageGeneration confirmMsg = new ConfirmMessageGeneration();
		confirmMsg.setMessage(bpmMessages);
		confirmMsg.setFrom(loadUnloadBag.getLoggedInAgent());

		// Call blockchain - persist

		return confirmMsg;
	}

}
