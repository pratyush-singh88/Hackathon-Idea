/**
 * 
 */
package com.bangalorewest.bagtracker.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bangalorewest.bagtracker.dto.PaxItinerary;
import com.bangalorewest.bagtracker.model.Booking;
import com.bangalorewest.bagtracker.repository.BookingRepository;
import com.bangalorewest.bagtracker.util.DateTimeUtil;

/**
 * @author sudhanshu.singh
 *
 */
@Component
public class BookingDAO {

	@Autowired
	BookingRepository bookingRepo;

	public List<PaxItinerary> getBookings(String dateString) {
		List<Booking> bookings = null;
		Date date = null;
		date = DateTimeUtil.parseStringDate(dateString);
		bookings = bookingRepo.findAllByDateOfFirstSegment(date);
		return maptoPaxItinerary(bookings);
	}

	private List<PaxItinerary> maptoPaxItinerary(List<Booking> bookings) {
		List<PaxItinerary> paxItineraries = new ArrayList<PaxItinerary>();
		for (Booking booking : bookings) {
			PaxItinerary paxItinerary = new PaxItinerary();
			paxItinerary.setNumberOfCheckedInBags(booking.getNumberOfCheckedInBags());
			paxItinerary.setDateOfTravel(DateTimeUtil.formatDateToString(booking.getDateOfFirstSegment()));
			paxItinerary.setEmailID(booking.getEmail());
			paxItinerary.setFirstName(booking.getFirstName());
			paxItinerary.setLastName(booking.getLastName());
			paxItinerary.setFlight(booking.getFlightdetails());
			paxItinerary.setListOfGeneratedBagTags(convertStringOfBagTagsToList(booking.getBagTags()));
			paxItineraries.add(paxItinerary);
		}
		return paxItineraries;
	}

	private List<String> convertStringOfBagTagsToList(String bagTags) {
		if (bagTags != null && !bagTags.isEmpty()) {
			String[] bagTagArray = bagTags.split(",");
			List<String> bagTagList = Arrays.asList(bagTagArray);
			return bagTagList;
		}
//		List<String> bagTagsWithoutQuantity = bagTagList.stream()
//				.map(bagTag -> bagTag.substring(0, bagTag.length() - 3)).collect(Collectors.toList());
//		return bagTagsWithoutQuantity;
		return null;
		
	}
}
