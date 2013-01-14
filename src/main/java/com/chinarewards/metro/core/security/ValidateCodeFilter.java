package com.chinarewards.metro.core.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * 验证码
 * @author huangshan
 *
 */
public class ValidateCodeFilter extends UsernamePasswordAuthenticationFilter{

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		
		String rand = (String) request.getSession().getAttribute("rand");
		String param = request.getParameter("rand");
		if(rand.equals(param)){
			return super.attemptAuthentication(request, response);
		}
		throw new AuthenticationServiceException("validate code is error");
	}
	
}
