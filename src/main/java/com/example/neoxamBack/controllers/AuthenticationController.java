package com.example.neoxamBack.controllers;

import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.services.AuthenticationRequest;
import com.example.neoxamBack.services.AuthenticationResponse;
import com.example.neoxamBack.services.AuthenticationService;
import com.example.neoxamBack.services.userService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {
    private final AuthenticationService service;
    @Autowired
    private userService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


}