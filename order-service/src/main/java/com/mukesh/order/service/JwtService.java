package com.mukesh.order.service;

import com.mukesh.order.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(AppUser user){
        return Jwts.builder()
                .claims(buildClaims(user))
                .subject(user.getUserName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails user) {

        String username = extractUsername(token);

        return username.equals(user.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }

    private Map<String, Object> buildClaims(AppUser user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put(
                "roles",
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .toList()
        );

        claims.put(
                "permissions",
                user.getRoles()
                        .stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(permission -> permission.getName().name())
                        .distinct()
                        .toList()
        );

        return claims;
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {

        return extractClaim(
                token,
                claims -> (List<String>) claims.get("roles")
        );
    }

    @SuppressWarnings("unchecked")
    public List<String> extractPermissions(String token) {

        return extractClaim(
                token,
                claims -> (List<String>) claims.get("permissions")
        );
    }


}
