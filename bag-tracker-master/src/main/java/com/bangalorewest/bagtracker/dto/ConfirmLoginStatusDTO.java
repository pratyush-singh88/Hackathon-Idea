/**
 * Created as part of Sabre hackathon 2019.
 */
package com.bangalorewest.bagtracker.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * LoginStatusDTO.java Created On: Oct 26, 2019 Created By: M1041768
 */
@JsonPropertyOrder("message")
public class ConfirmLoginStatusDTO {

	/**
	 * @param userName
	 * @param userRole
	 */
	public ConfirmLoginStatusDTO(String userName, String userRole, String message, String agentLocation) {
		super();
		this.userName = userName;
		this.userRole = userRole;
		this.message = message;
		this.agentLocation = agentLocation;
	}

	/**
	 * 
	 */
	public ConfirmLoginStatusDTO() {
		super();
	}

	private String userName;

	private String userRole;

	private String message;

	private String agentLocation;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return userName;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.userName = email;
	}

	/**
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the agentLocation
	 */
	public String getAgentLocation() {
		return agentLocation;
	}

	/**
	 * @param agentLocation the agentLocation to set
	 */
	public void setAgentLocation(String agentLocation) {
		this.agentLocation = agentLocation;
	}

}
