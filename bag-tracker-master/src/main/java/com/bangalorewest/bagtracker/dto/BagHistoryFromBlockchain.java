package com.bangalorewest.bagtracker.dto;

/**
 * @author sudhanshu.singh
 *
 */
public class BagHistoryFromBlockchain {

	private String bagTagID;
	private String airportCode;
	private String timestamp;
	private String from;
	private String to;
	private String message;
	private String messageDate;

	/**
	 * @return the bagTagID
	 */
	public final String getBagTagID() {
		return bagTagID;
	}

	/**
	 * @param bagTagID the bagTagID to set
	 */
	public final void setBagTagID(String bagTagID) {
		this.bagTagID = bagTagID;
	}

	/**
	 * @return the airportCode
	 */
	public final String getAirportCode() {
		return airportCode;
	}

	/**
	 * @param airportCode the airportCode to set
	 */
	public final void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	/**
	 * @return the timestamp
	 */
	public final String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public final void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the from
	 */
	public final String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public final void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public final String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public final void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public final void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the messageDate
	 */
	public final String getMessageDate() {
		return messageDate;
	}

	/**
	 * @param messageDate the messageDate to set
	 */
	public final void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}
}
