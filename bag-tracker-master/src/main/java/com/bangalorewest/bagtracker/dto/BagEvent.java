/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

/**
 * @author sudhanshu.singh
 *
 */
public class BagEvent {
	
	private String bagTagID;
	private String message;
	private String bagDate;
	private String from;
	private String to;
	private String flight;

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
	 * @return the bagDate
	 */
	public String getBagDate() {
		return bagDate;
	}

	/**
	 * @param bagDate the bagDate to set
	 */
	public void setBagDate(String bagDate) {
		this.bagDate = bagDate;
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

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}


}
