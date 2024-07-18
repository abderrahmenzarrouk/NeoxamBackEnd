package com.example.neoxamBack.services;

import com.example.neoxamBack.repositories.userRepository;
import com.example.neoxamBack.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final userRepository userRepository;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var Token = JwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(Token)
                    .user(user)
                    .build();
        }catch (AuthenticationException e) {

            e.printStackTrace();
            throw e;
        }
    }
}
