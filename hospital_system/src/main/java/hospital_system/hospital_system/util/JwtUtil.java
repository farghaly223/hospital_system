package hospital_system.hospital_system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username, String role) {
        System.out.println("DEBUG JWT: Generating token for user: " + username + " with role: " + role);
        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("DEBUG JWT: Token generated successfully");
        return token;
    }

    public String extractUsername(String token) {
        try {
            String username = extractAllClaims(token).getSubject();
            System.out.println("DEBUG JWT: Extracted username: " + username);
            return username;
        } catch (Exception e) {
            System.out.println("DEBUG JWT: Error extracting username: " + e.getMessage());
            return null;
        }
    }

    public String extractRole(String token) {
        try {
            String role = extractAllClaims(token).get("role", String.class);
            System.out.println("DEBUG JWT: Extracted role: " + role);
            return role;
        } catch (Exception e) {
            System.out.println("DEBUG JWT: Error extracting role: " + e.getMessage());
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("DEBUG JWT: Claims extracted successfully. All claims: " + claims);
            return claims;
        } catch (Exception e) {
            System.out.println("DEBUG JWT: Error extracting claims: " + e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            System.out.println("DEBUG JWT: Token validation successful");
            return true;
        } catch (Exception e) {
            System.out.println("DEBUG JWT: Token validation failed: " + e.getMessage());
            return false;
        }
    }
}
