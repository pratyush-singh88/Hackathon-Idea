/**
 * 
 */
package com.bangalorewest.bagtracker.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bangalorewest.bagtracker.dto.BlockChainLoginResponse;

/**
 * @author sudhanshu.singh
 *
 */
//@Component
public class LoginHelper {

//	private static RestTemplate restTemplate;
//
//	@Autowired
//	public LoginHelper(RestTemplate restTemplate) {
//		// TODO Auto-generated constructor stub
//		LoginHelper.restTemplate = restTemplate;
//	}

	public static String issueBlockChainSecurityToken() {
		RestTemplate restTemplate = new RestTemplate();
		String loginURL = ConfigReader.getBaseUrl().concat(ConfigReader.getLoginUrl());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String requestJSON = "{\"orgName\" : \"" + "BLR" + "\", \"verificationCode\" : " + 321 + "}";
		HttpEntity<String> request = new HttpEntity<String>(requestJSON, headers);
		ResponseEntity<BlockChainLoginResponse> bearerToken = restTemplate.postForEntity(loginURL, request,
				BlockChainLoginResponse.class);
		String authToken = "";
		if (bearerToken != null && bearerToken.getBody() != null) {
			authToken = bearerToken.getBody().getData().getToken();
		}
		return authToken;
	}

}
