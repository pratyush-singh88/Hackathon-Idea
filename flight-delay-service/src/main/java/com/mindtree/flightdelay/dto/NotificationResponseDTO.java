/**
 * 
 */
package com.mindtree.flightdelay.dto;

/**
 * @author M1038389
 *
 */
public class NotificationResponseDTO {
	
	private String email;
	private String message;
	private String failed;
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the failed
	 */
	public String getFailed() {
		return failed;
	}
	/**
	 * @param failed the failed to set
	 */
	public void setFailed(String failed) {
		this.failed = failed;
	}

}
