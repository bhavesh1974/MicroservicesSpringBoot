package com.bhaveshshah.authservice.controller;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/auth")
public class ServiceController {
	@PostMapping("/gettoken")
	public ResponseEntity<Object> getToken(@RequestBody HashMap<String,String> requestBody){
		String userName = requestBody.get("userName");
		String password = requestBody.get("password");
		if (userName == null || password == null) {
			return new ResponseEntity<>(new String("User or Password is missing."), HttpStatus.UNAUTHORIZED);
		}
		
		if (!userName.equals("bhavesh") || !password.equals("12345678")) {
			return new ResponseEntity<>(new String("User or Password is invalid"), HttpStatus.UNAUTHORIZED);
		}
		
		//Add expiration to 7 days
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 7);
		//Generate Token
		String jwt = "";		
		try {
			jwt = Jwts.builder()
					  .setSubject("speak")
					  .setExpiration(cal.getTime())
					  .claim("userName", "bhavesh")
					  .claim("generatedBy", "AuthService")
					  .signWith(
					    SignatureAlgorithm.HS256,
					    "speakSuperJWTSecret".getBytes("UTF-8")
					  )
					  .compact();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Prepared response to return in JSON
		Map<String, Object> response = new HashMap<>();
		response.put("token", jwt);
		response.put("code", "200");
		
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}

}
