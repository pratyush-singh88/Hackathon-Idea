/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

import java.util.Date;

/**
 * @author sudhanshu.singh
 *
 */
public class BagHistoryDTO {

	private String status;
	private String timestamp;
	private String from;
	private String to;
	private String airportCode;
	private String messageDate;
	private String arrivalFlight;
	private String departureFlight;
	private String connectionFlight;
	private String loadFlight;
	private String bagTagID;
	private String firstName;
	private String lastName;
	private Integer numberOfCheckedInBags;

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
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
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
	 * @return the messageDate
	 */
	public String getMessageDate() {
		return messageDate;
	}

	/**
	 * @param messageDate the messageDate to set
	 */
	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}

	/**
	 * @return the arrivalFlight
	 */
	public String getArrivalFlight() {
		return arrivalFlight;
	}

	/**
	 * @param arrivalFlight the arrivalFlight to set
	 */
	public void setArrivalFlight(String arrivalFlight) {
		this.arrivalFlight = arrivalFlight;
	}

	/**
	 * @return the departureFlight
	 */
	public String getDepartureFlight() {
		return departureFlight;
	}

	/**
	 * @param departureFlight the departureFlight to set
	 */
	public void setDepartureFlight(String departureFlight) {
		this.departureFlight = departureFlight;
	}

	/**
	 * @return the connectionFlight
	 */
	public String getConnectionFlight() {
		return connectionFlight;
	}

	/**
	 * @param connectionFlight the connectionFlight to set
	 */
	public void setConnectionFlight(String connectionFlight) {
		this.connectionFlight = connectionFlight;
	}

	/**
	 * @return the loadFlight
	 */
	public String getLoadFlight() {
		return loadFlight;
	}

	/**
	 * @param loadFlight the loadFlight to set
	 */
	public void setLoadFlight(String loadFlight) {
		this.loadFlight = loadFlight;
	}

	/**
	 * @return the bagTagID
	 */
	public String getBagTagID() {
		return bagTagID;
	}

	/**
	 * @param bagTagID the bagTagID to set
	 */
	public void setBagTagID(String bagTagID) {
		this.bagTagID = bagTagID;
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

	/**
	 * @return the numberOfCheckedInBags
	 */
	public Integer getNumberOfCheckedInBags() {
		return numberOfCheckedInBags;
	}

	/**
	 * @param numberOfCheckedInBags the numberOfCheckedInBags to set
	 */
	public void setNumberOfCheckedInBags(Integer numberOfCheckedInBags) {
		this.numberOfCheckedInBags = numberOfCheckedInBags;
	}

}
