/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

import java.util.Date;

/**
 * @author sudhanshu.singh
 *
 */
public class RetrieveDetails {

	private String message;
	private String bagTagID;
	private Date timestamp;
	private String airportCode;
	private String from;
	private String to;
	private String messageDate;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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
	 * @return the bagDate
	 */
	public String getBagDate() {
		return messageDate;
	}

	/**
	 * @param bagDate the bagDate to set
	 */
	public void setBagDate(String messageDate) {
		this.messageDate = messageDate;
	}

}
