package com.wajeed.ecommerce.security;

import com.wajeed.ecommerce.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    public String secretKey;

    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(Users users) {

        String roleWithPrefix = "ROLE_" + users.getRole().name();

        return Jwts.builder()
                .setSubject(users.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60)
                )
                .claim("role", roleWithPrefix)
                .signWith(secretKey())
                .compact();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractAllClaims(jwt).getExpiration();
    }

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    public String extractRole(String jwt) {
        return extractAllClaims(jwt).get("role", String.class);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(secretKey())
                .parseClaimsJws(jwt)
                .getBody();
    }
}