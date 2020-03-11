/**
 * Created as part of Osmosis 2020.
 */
package com.bangalorewest.bagtracker.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bangalorewest.bagtracker.model.Booking;

/**
 * 
 * BookingRepository.java Created On: Feb 24, 2020 Created By: M1026329
 */
public interface BookingRepository extends JpaRepository<Booking, Integer> {

	List<Booking> findAllByDateOfFirstSegment(Date dateOfFirstSegment);

	@Modifying
	@Transactional
	@Query(value = "update BOOKING_INFORMATION set BAG_TAG_IDS = :bagTagIDs, NO_OF_CHECKEDIN_BAGS = :numberOfBags where EMAIL_ID=:email AND FIRST_NAME=:fname AND DATE_OF_FIRST_SEGMENT=:travelDate", nativeQuery = true)
	Integer updateGeneratedBagTagsToBookings(@Param("bagTagIDs") String bagTagIDs, @Param("email") String email,
			@Param("fname") String firstName, @Param("travelDate") Date dateOfTravel, @Param("numberOfBags") Integer numberOfCheckedInBags);

}
