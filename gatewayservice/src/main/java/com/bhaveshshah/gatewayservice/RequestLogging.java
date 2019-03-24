package com.bhaveshshah.gatewayservice;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Component
public class RequestLogging extends ZuulFilter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		boolean isAuth = false;
		if( request.getRequestURI().equals("/auth/gettoken") || 
			request.getMethod().equals("OPTIONS")) {
			isAuth=true;
		} else {	
			String authorization = request.getHeader("Authorization");
			if (authorization != null) {
				if (authorization.length() > 7) {
					String token = authorization.substring(7);
					Jws<Claims> claims = null;
					try {
						claims = Jwts.parser()
								  .setSigningKey("speakSuperJWTSecret".getBytes("UTF-8"))
								  .parseClaimsJws(token);
						String userName = (String) claims.getBody().get("userName");
						String generatedBy = (String) claims.getBody().get("generatedBy");
						
						if (userName.equals("bhavesh") && generatedBy.equals("AuthService")) {
							isAuth = true;
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
		} 

		//verify authentication token
		if (!isAuth) {
			context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			context.setResponseBody("Not Authorized");
			context.setSendZuulResponse(false);
			return null;
		}

		//Add unique request id to use in the logging
		String requestId = UUID.randomUUID().toString();
		context.addZuulRequestHeader("requestid", requestId);
		logger.info("Request: " + requestId + " - " + request.getRequestURI() + " forwarded.");
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "pre";
	}

}
