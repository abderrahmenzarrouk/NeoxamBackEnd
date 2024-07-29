package com.example.neoxamBack.controllers;

import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.entities.changepasswordRequest;
import com.example.neoxamBack.services.mailService;
import com.example.neoxamBack.services.userService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin("*")
public class userController {
    @Autowired
    userService userService;
    @Autowired
    private com.example.neoxamBack.services.mailService mailService;
    @PutMapping("/reset-password/{id}")
    public String resetpassword(@PathVariable Long id, @RequestBody changepasswordRequest changepasswordRequest) {
        return userService.resetpassword(id,changepasswordRequest.getOldpassword(),changepasswordRequest.getNewpassword());
    }
    @GetMapping("/allusers")
    public List<User> getallusers() {
        return userService.getallusers();
    }
    @PutMapping("/acceptuser/{email}")
    public User acceptuser(@PathVariable String email) {
        User u =  userService.acceptuser(email);
        mailService.sendEmailaccept(email);
        return u;
    }
    @PutMapping("/banuser/{email}")
    public User banuser(@PathVariable String email) {
        User u =  userService.banuser(email);
        mailService.sendEmailban(email);
        return u;
    }

}
