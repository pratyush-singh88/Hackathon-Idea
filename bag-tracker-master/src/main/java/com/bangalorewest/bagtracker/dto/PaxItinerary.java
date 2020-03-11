/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

import java.util.Date;
import java.util.List;

/**
 * @author sudhanshu.singh
 *
 */
public class PaxItinerary {
	private String flight;
	private String firstName;
	private String lastName;
	private String dateOfTravel;
	private String emailID;
	private Integer numberOfCheckedInBags;
	private String loggedInAgent;
	private List<String> listOfGeneratedBagTags;

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
	 * @return the dateOfTravel
	 */
	public String getDateOfTravel() {
		return dateOfTravel;
	}

	/**
	 * @param dateOfTravel the dateOfTravel to set
	 */
	public void setDateOfTravel(String dateOfTravel) {
		this.dateOfTravel = dateOfTravel;
	}

	/**
	 * @return the emailID
	 */
	public String getEmailID() {
		return emailID;
	}

	/**
	 * @param emailID the emailID to set
	 */
	public void setEmailID(String emailID) {
		this.emailID = emailID;
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

	/**
	 * @return the loggedInAgent
	 */
	public String getLoggedInAgent() {
		return loggedInAgent;
	}

	/**
	 * @param loggedInAgent the loggedInAgent to set
	 */
	public void setLoggedInAgent(String loggedInAgent) {
		this.loggedInAgent = loggedInAgent;
	}

	/**
	 * @return the listOfGeneratedBagTags
	 */
	public List<String> getListOfGeneratedBagTags() {
		return listOfGeneratedBagTags;
	}

	/**
	 * @param listOfGeneratedBagTags the listOfGeneratedBagTags to set
	 */
	public void setListOfGeneratedBagTags(List<String> listOfGeneratedBagTags) {
		this.listOfGeneratedBagTags = listOfGeneratedBagTags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PaxItinerary [flight=" + flight + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", dateOfTravel=" + dateOfTravel + ", emailID=" + emailID + ", numberOfCheckedInBags="
				+ numberOfCheckedInBags + ", loggedInAgent=" + loggedInAgent + ", listOfGeneratedBagTags="
				+ listOfGeneratedBagTags + "]";
	}

}
