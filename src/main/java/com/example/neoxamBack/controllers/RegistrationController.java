package com.example.neoxamBack.controllers;

import com.example.neoxamBack.entities.Roles;
import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.repositories.roleRepository;
import com.example.neoxamBack.services.ExcelService;
import com.example.neoxamBack.services.RegistrationRequest;
import com.example.neoxamBack.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping(path = "api/v1/auth/registration")
@AllArgsConstructor
@CrossOrigin("*")
public class RegistrationController {

    private RegistrationService registrationService;
    @Autowired
    private roleRepository roleR;
    @Autowired
    private ExcelService excelService;
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

    @PostMapping("/upload/{version}")
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file,@PathVariable int version) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid Excel file.");
        }

        try {
            excelService.saveExcelData(file,version);
            return ResponseEntity.status(HttpStatus.OK).body("Excel file data imported successfully.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while importing the Excel file.");
        }
    }



}
