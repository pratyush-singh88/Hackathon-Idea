/**
 * 
 */
package com.bangalorewest.bagtracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author sudhanshu.singh
 *
 */
@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class InvalidBagTagException extends Exception {

	private static final long serialVersionUID = -7969144027811454149L;

	public InvalidBagTagException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
