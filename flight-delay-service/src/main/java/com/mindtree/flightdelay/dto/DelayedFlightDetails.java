package com.mindtree.flightdelay.dto;

import java.math.BigInteger;

/**
 * 
 * @author M1021548
 *
 */
public class DelayedFlightDetails {
	
	private String airline;
	
	private String flightNumber;
	
	private String flightDate;
	
	private String predictedDepartureDelay;

	/**
	 * @return the airline
	 */
	public String getAirline() {
		return airline;
	}

	/**
	 * @param airline the airline to set
	 */
	public void setAirline(String airline) {
		this.airline = airline;
	}

	/**
	 * @return the flight_Number
	 */
	public String getFlightNumber() {
		return flightNumber;
	}

	/**
	 * @param flight_Number the flight_Number to set
	 */
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	/**
	 * @return the flightDate
	 */
	public String getFlightDate() {
		return flightDate;
	}

	/**
	 * @param flightDate the flightDate to set
	 */
	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}

	/**
	 * @return the predictedDepartureDelay
	 */
	public String getPredictedDepartureDelay() {
		return predictedDepartureDelay;
	}

	/**
	 * @param predictedDepartureDelay the predictedDepartureDelay to set
	 */
	public void setPredictedDepartureDelay(String predictedDepartureDelay) {
		this.predictedDepartureDelay = predictedDepartureDelay;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DelayedFlightDetailsResponse [airline=" + airline + ", flight_Number=" + flightNumber
				+ ", FlightDate=" + flightDate + ", predictedDepartureDelay="
				+ predictedDepartureDelay + "]";
	}
	
	

}
