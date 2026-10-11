package com.george.user.service.impl;

import com.george.user.entity.User;
import com.george.user.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {


    private final Key key;
    private final long expiration;

    public JwtServiceImpl(@Value("${security.jwt.secret}") String secret,
                          @Value("${security.jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiration = expiration;
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getUserId())
                .claim("role", user.getUserRole().name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }
}
