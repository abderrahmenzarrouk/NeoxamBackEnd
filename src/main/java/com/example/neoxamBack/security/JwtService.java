package com.example.neoxamBack.security;

import com.example.neoxamBack.entities.User;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    private static final String SECRET_KEY = "9CD2AA413D4E846C5EC9F9FAB37DBJHRJAKHEUHREZJKREZJHREZJFNZKJEGFHEZ";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public static String generateToken(User userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    private static String generateToken(
            Map<String, Object> extraClaims,
            User userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3000 * 60 *24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token, User userDetails) {
        final String username = extractUsername(token);
        System.out.println("istoken valide username = " + userDetails.getUsername());

        return (username.equals(userDetails.getEmail())) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

