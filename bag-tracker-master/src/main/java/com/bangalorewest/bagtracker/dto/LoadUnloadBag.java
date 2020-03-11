/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

/**
 * @author sudhanshu.singh
 *
 */
public class LoadUnloadBag {

	private String dateOfTravel;
	private String bagTagID;
	private String loggedInAgent;
	private String uldContainer;
	private String agentLocation;

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
	 * @return the uldContainer
	 */
	public String getUldContainer() {
		return uldContainer;
	}

	/**
	 * @param uldContainer the uldContainer to set
	 */
	public void setUldContainer(String uldContainer) {
		this.uldContainer = uldContainer;
	}

	public String getAgentLocation() {
		return agentLocation;
	}

	public void setAgentLocation(String agentLocation) {
		this.agentLocation = agentLocation;
	}

}
