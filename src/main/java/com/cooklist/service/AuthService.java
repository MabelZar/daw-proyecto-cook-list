package com.cooklist.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cooklist.dto.LoginRequestDto;
import com.cooklist.dto.LoginResponseDto;
import com.cooklist.dto.UserResponseDto;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final long expiration;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService,
            @Value("${JWT_EXPIRATION:7200000}") long expiration) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.expiration = expiration;
    }

    public LoginResponseDto login(LoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        String token = jwtService.generateToken(authentication.getName());

        UserResponseDto userResponseDto = userService.findByEmail(loginRequest.email());

        return new LoginResponseDto(
                token,
                "Bearer",
                expiration,
                userResponseDto
        );
    }
}