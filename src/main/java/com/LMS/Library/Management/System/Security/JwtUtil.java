package com.LMS.Library.Management.System.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secrect;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secrect);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String genrateToken(Integer userId, String email, String role){

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();


    }

    /**
     * Extract All Claims
     */
    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    /**
     * User Id
     */
    public Integer extractUserId(String token) {

        return extractAllClaims(token)
                .get("userId", Integer.class);
    }
    /**
    * Email
     */
    public String extractEmail(String token) {

        return extractAllClaims(token)
                .get("email", String.class);
    }


    /**
     * Role
     */
    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }



    /**
     * Expiration
     */
    public Date extractExpiration(String token) {

        return extractAllClaims(token).getExpiration();
    }

    /**
     * Token Expired?
     */
    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    /**
     * Validate Token
     */
    public boolean validateToken(String token) {

        try {

            return !isTokenExpired(token);

        } catch (JwtException | IllegalArgumentException e) {

            return false;
        }
    }
}
