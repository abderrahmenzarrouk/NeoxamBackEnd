package com.example.neoxamBack.controllers;

import com.example.neoxamBack.entities.Roles;
import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.repositories.roleRepository;
import com.example.neoxamBack.services.RegistrationRequest;
import com.example.neoxamBack.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "api/v1/auth/registration")
@AllArgsConstructor
@CrossOrigin("*")
public class RegistrationController {

    private RegistrationService registrationService;
    @Autowired
    private roleRepository roleR;
    @PostMapping
    public String register(@RequestBody User user
){
        System.out.println("hello");
        System.out.println(user.getUserRole());
        RegistrationRequest request = new RegistrationRequest(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getUserRole());
        System.out.println("request");
        System.out.println(request);
        return registrationService.register(request );
    }





}
