/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

/**
 * @author sudhanshu.singh
 *
 */
public class Status {
	private String statusCode;

	private String message;

	/**
	 * @return the statusCode
	 */
	public final String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public final void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public final void setMessage(String message) {
		this.message = message;
	}

}
