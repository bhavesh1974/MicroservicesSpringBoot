package com.bhaveshshah.orderservice.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.portable.OutputStream;

public class CallAPI {
	public static String call(String url, String method, HttpServletRequest request) {
		try {
			//Open endpoint
			URL endPoint = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) endPoint.openConnection();
			//Set connection properties
			connection.setReadTimeout(60000);
			connection.setConnectTimeout(60000);
			connection.setDoInput(true);
			connection.setRequestMethod(method);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", request.getHeader("Authorization"));
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
				return "HTTP_NOT_FOUND";
			}
			
			//Read response
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response;
			StringBuilder apiResponse = new StringBuilder("");
			while ((response = br.readLine()) != null) {
				apiResponse.append(response);
			}
			return apiResponse.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "HTTP_NOT_FOUND";
	}
}
