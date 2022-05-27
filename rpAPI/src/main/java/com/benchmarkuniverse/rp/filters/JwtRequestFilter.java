package com.benchmarkuniverse.rp.filters;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.benchmarkuniverse.rp.exception.UnAuthorizedException;
import com.benchmarkuniverse.rp.util.JwtUtil;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Value("${jwt.key}")
	private static String secretKey;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");
		String jwt = null;
		String username=null;
		if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			 throw new UnAuthorizedException(UnAuthorizedException.Issue.UNAUTHORIZED_ACCESS, "Unauthorized access");

		}
		
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			boolean issingned=jwtUtil.isSignedToken(jwt);
			username = jwtUtil.extractUsername(jwt);
			if ( jwtUtil.validate(jwt) ) { 
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authentication);
				 chain.doFilter(request, response);
			}else {
				 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				 throw new UnAuthorizedException(UnAuthorizedException.Issue.UNAUTHORIZED_ACCESS, "Unauthorized access");

			}
		}		
	}


}