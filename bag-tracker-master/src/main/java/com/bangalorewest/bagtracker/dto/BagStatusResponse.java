/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

/**
 * @author sudhanshu.singh
 */
public class BagStatusResponse {

	private String status;
	// BSM=BAG CHECKEDIN, BPM LOAD-BAG LOADED ONTO CONTAINER
	// BPM OFF = BAG OFFLOADED, HLD= BAG LOADED ONTO AIRCRAFT

	private String flight; // AI101
	private String date; // .F
	private String airportCode; // .v
	private String firstName;
	private String lastName;

	private BPMStatusResponse BPMStatusResponse;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the flight
	 */
	public String getFlight() {
		return flight;
	}

	/**
	 * @param flight the flight to set
	 */
	public void setFlight(String flight) {
		this.flight = flight;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the airportCode
	 */
	public String getAirportCode() {
		return airportCode;
	}

	/**
	 * @param airportCode the airportCode to set
	 */
	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	/**
	 * @return the bPMStatusResponse
	 */
	public BPMStatusResponse getBPMStatusResponse() {
		return BPMStatusResponse;
	}

	/**
	 * @param bPMStatusResponse the bPMStatusResponse to set
	 */
	public void setBPMStatusResponse(BPMStatusResponse bPMStatusResponse) {
		BPMStatusResponse = bPMStatusResponse;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
