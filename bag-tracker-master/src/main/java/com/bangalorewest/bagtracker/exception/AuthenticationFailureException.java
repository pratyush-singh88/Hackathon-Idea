/**
 * Created as part of Sabre hackathon 2019.
 */
package com.bangalorewest.bagtracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * AuthenticationFailureException.java
 * Created On: Oct 26, 2019
 * Created By: M1041768
 */
@ResponseStatus(code=HttpStatus.UNAUTHORIZED)
public class AuthenticationFailureException extends Exception {
    
    /**
     * 
     */
    public AuthenticationFailureException(String msg) {
        super(msg);
    }

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2306613846490237418L;

}
