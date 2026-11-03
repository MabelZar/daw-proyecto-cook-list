package com.cooklist.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.cooklist.dto.LoginRequestDto;
import com.cooklist.dto.LoginResponseDto;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final long expiration;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            @Value("${JWT_EXPIRATION:7200000}") long expiration) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.expiration = expiration;
    }

    public LoginResponseDto login(LoginRequestDto loginRequest) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        String token = jwtService.generateToken(authentication.getName());

        return new LoginResponseDto(
                token,
                "Bearer",
                expiration
        );
    }
}