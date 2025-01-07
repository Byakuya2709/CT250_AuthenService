package com.example.utils;

import com.example.model.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Thời gian hết hạn token (15 phút)
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    // Tạo token từ thông tin account
    public String generateToken(Account account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", account.getType().toString());
        claims.put("userId", account.getId());
        return createToken(claims, account.getEmail());
    }

  private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Trích xuất username (email) từ token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Trích xuất role từ token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // Trích xuất userId từ token
    public String extractUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    // Kiểm tra token có hết hạn không
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Kiểm tra token có hợp lệ không (so sánh email hoặc id)
    public boolean validateToken(String token, String expectedEmail) {
        final String username = extractUsername(token);
        return (username.equals(expectedEmail) && !isTokenExpired(token));
    }

    // Trích xuất tất cả claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
