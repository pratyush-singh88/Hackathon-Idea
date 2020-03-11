/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

import com.bangalorewest.bagtracker.util.PasswordEncryptionUtil;

/**
 * @author sudhanshu.singh
 *
 */
public class LoginDTO {

	private String userName;
	private String userPassword;
	private String userRole;
	private String agentLocation;

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
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = PasswordEncryptionUtil.hashPassword(userPassword);
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
		this.userRole = userRole.toUpperCase();
	}

	/**
	 * @return the agentLocation
	 */
	public final String getAgentLocation() {
		return agentLocation;
	}

	/**
	 * @param agentLocation the agentLocation to set
	 */
	public final void setAgentLocation(String agentLocation) {
		this.agentLocation = agentLocation;
	}

}
