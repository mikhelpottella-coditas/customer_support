package com.example.customersupport.util;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String secretString;

    public JwtUtil(@Value("${secret}")
                   String secretString) {
        this.secretString = secretString;
    }




    public String generateToken(String username){


        return Jwts.builder()
                .claims()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+(1000*60*30)))
                .and()
                .signWith(Keys.hmacShaKeyFor(secretString.getBytes()),Jwts.SIG.HS256)
                .compact();
    }



    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretString.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String returnSubject(String token){
        System.out.println(extractClaims(token).getSubject());
        return extractClaims(token).getSubject();
    }


    public Boolean validateToken(UserDetails userDetails, String token){
        String username=  extractClaims(token).getSubject();
        return (username.equals(userDetails.getUsername())&& !isExpired(token));
    }

    public boolean isExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }


}
