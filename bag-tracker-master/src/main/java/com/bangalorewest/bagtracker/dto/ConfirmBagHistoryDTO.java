/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

import java.util.List;

/**
 * @author M1038389
 *
 */
public class ConfirmBagHistoryDTO {
	
	private String airportCode;
	
	private List<BagHistoryDTO> bagHistoryDTOs;
	
	public ConfirmBagHistoryDTO() {
		
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
	 * @return the bagHistoryDTOs
	 */
	public List<BagHistoryDTO> getBagHistoryDTOs() {
		return bagHistoryDTOs;
	}

	/**
	 * @param bagHistoryDTOs the bagHistoryDTOs to set
	 */
	public void setBagHistoryDTOs(List<BagHistoryDTO> bagHistoryDTOs) {
		this.bagHistoryDTOs = bagHistoryDTOs;
	}

	@Override
	public String toString() {
		return "ConfirmBagHistoryDTO [airportCode=" + airportCode + ", bagHistoryDTOs=" + bagHistoryDTOs + "]";
	}
	
	

}
