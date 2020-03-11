/**
 * 
 */
package com.bangalorewest.bagtracker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author sudhanshu.singh
 *
 */
@Entity
public class Agent {

	@Id
	@GeneratedValue
	private Integer agentID;

	private String userName;

	private String password;

	private String role;

	private String agentLocation;

	/**
	 * @return the agentID
	 */
	public Integer getAgentID() {
		return agentID;
	}

	/**
	 * @param agentID the agentID to set
	 */
	public void setAgentID(Integer agentID) {
		this.agentID = agentID;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
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
