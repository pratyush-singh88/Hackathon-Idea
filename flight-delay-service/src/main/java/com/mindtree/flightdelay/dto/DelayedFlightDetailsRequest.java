package com.mindtree.flightdelay.dto;

/**
 * 
 * @author M1021548
 *
 */
public class DelayedFlightDetailsRequest {
	
	private String originCity;
	
	private String airline;
	
	private String flightDate;

	/**
	 * @return the originCity
	 */
	public String getOriginCity() {
		return originCity;
	}

	/**
	 * @param originCity the originCity to set
	 */
	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DelayedFlightDetailsRequest [originCity=" + originCity + ", airline=" + airline + ", flightDate="
				+ flightDate + "]";
	}
	
	

}
