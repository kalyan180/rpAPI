package com.benchmarkuniverse.rp.util;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtUtil {
	
	@Value("${skipAuthorization}")
	private boolean skipAuthorization;
	
	@Value("${jwt.key}")
    private  String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public boolean validate(String token){
        if(skipAuthorization){
            return true;
        }
        try{
            Claims claims = extractAllClaims(token);
            if (claims != null){
                return true;
            }
        }catch(Exception ex){
               return false;
        }
        return false;
    }
    public boolean isSignedToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes()).isSigned(token);
    }

}