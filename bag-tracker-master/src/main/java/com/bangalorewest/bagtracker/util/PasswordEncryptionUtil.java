/**
 * Created as part of Sabre hackathon 2019.
 */
package com.bangalorewest.bagtracker.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 
 * PasswordEncryptionUtil.java Created On: Oct 26, 2019 Created By: M1041768
 */
public class PasswordEncryptionUtil {

	/**
	 * Description : <<WRITE DESCRIPTION HERE>>
	 * 
	 * @param password2
	 * @return
	 */
	public static String hashPassword(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, ConfigReader.getPwdEncryption());
	}

}
