package com.benchmarkuniverse.rp.filters;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter   {

	// Details omitted for brevity
	@Autowired
	private JwtRequestFilter jwtTokenFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Enable CORS and disable CSRF
		http = http.cors().and().csrf().disable();

		// Set session management to stateless
		http = http.httpBasic().and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and();
 
         		// Set unauthorized requests exception handler
		       http = http.exceptionHandling().authenticationEntryPoint((request, response, ex) ->
			     response.sendError(HttpServletResponse.SC_UNAUTHORIZED,ex.getMessage() )
		       ).and();
		// Set permissions on endpoints
		        http.authorizeRequests().anyRequest().authenticated();
		// Add JWT token filter
		        http.addFilterBefore(jwtTokenFilter,UsernamePasswordAuthenticationFilter.class);
	}

}
