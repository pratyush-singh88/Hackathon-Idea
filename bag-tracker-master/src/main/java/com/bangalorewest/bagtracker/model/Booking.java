/**
 * 
 */
package com.bangalorewest.bagtracker.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author M1026329
 *
 */
@Entity(name = "BOOKING_INFORMATION")
public class Booking {

	/**
	 * bookingId
	 */
	@Id
	@GeneratedValue
	@Column(name = "BOOKING_ID")
	private Integer bookingId;

	/**
	 * email
	 */
	@Column(name = "EMAIL_ID")
	private String email;

	/**
	 * firstName
	 */
	@Column(name = "FIRST_NAME")
	private String firstName;

	/**
	 * lastName
	 */
	@Column(name = "LAST_NAME")
	private String lastName;

	/**
	 * numberOfCheckedInBags.
	 */
	@Column(name = "NO_OF_CHECKEDIN_BAGS")
	private Integer numberOfCheckedInBags;

	/**
	 * dateOfFirstSegment
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_FIRST_SEGMENT")
	private Date dateOfFirstSegment;

	@Column(name = "BAG_TAG_IDS")
	private String bagTags;

	/**
	 * flightdetails.
	 */
	@Column(name = "FLIGHT_DETAILS")
	private String flightdetails;

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getNumberOfCheckedInBags() {
		return numberOfCheckedInBags;
	}

	public void setNumberOfCheckedInBags(Integer numberOfCheckedInBags) {
		this.numberOfCheckedInBags = numberOfCheckedInBags;
	}

	public Date getDateOfFirstSegment() {
		return dateOfFirstSegment;
	}

	public void setDateOfFirstSegment(Date dateOfFirstSegment) {
		this.dateOfFirstSegment = dateOfFirstSegment;
	}

	public String getFlightdetails() {
		return flightdetails;
	}

	public void setFlightdetails(String flightdetails) {
		this.flightdetails = flightdetails;
	}

	/**
	 * @return the bagTags
	 */
	public String getBagTags() {
		return bagTags;
	}

	/**
	 * @param bagTags the bagTags to set
	 */
	public void setBagTags(String bagTags) {
		this.bagTags = bagTags;
	}

}
