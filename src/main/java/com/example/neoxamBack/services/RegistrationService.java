package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.Roles;
import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.repositories.roleRepository;
import com.example.neoxamBack.security.emailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final userService userService;
    private roleRepository roleRepository;
    public String register(RegistrationRequest request) {
        Roles existingRole = roleRepository.findByRole(request.getRole().getRole());
        String token =  userService.signUpUser( new User(
                request.getLastName(),
                request.getFirstName(),
                request.getEmail(),
                request.getPassword(),
                existingRole
        ));
        return token;
    }


}

