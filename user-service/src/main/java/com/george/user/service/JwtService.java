package com.george.user.service;

import com.george.user.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateToken(User user);

    Claims extractAllClaims(String token);

}
