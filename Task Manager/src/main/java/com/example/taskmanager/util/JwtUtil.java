package com.example.taskmanager.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {


    private String secret =
            "mysecretkeymysecretkeymysecretkey123456789kkdkdkdkdkdkkdkd";


    public String extractUsername(String token){

        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }



    public String generateToken(String username){

        return Jwts.builder()
                .setSubject(username)

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )

                .signWith(
                        SignatureAlgorithm.HS256,
                        secret
                )

                .compact();
    }
}