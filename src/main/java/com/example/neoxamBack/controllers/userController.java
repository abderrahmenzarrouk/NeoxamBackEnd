package com.example.neoxamBack.controllers;

import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.entities.assignee;
import com.example.neoxamBack.entities.changepasswordRequest;
import com.example.neoxamBack.repositories.assigneeRepository;
import com.example.neoxamBack.repositories.userRepository;
import com.example.neoxamBack.services.mailService;
import com.example.neoxamBack.services.userService;
import lombok.AllArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
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

    @Autowired
    private userRepository userRepository;

    @Autowired
    private assigneeRepository assigneeRepository;

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
    @GetMapping("/users/byrole/{id}")
    public List<User> getmanagers(@PathVariable int id) {
        return userRepository.findByUserRoleId(id);
    }

    @PutMapping("/assigne-manager-to-multiple-assignee/{idmanager}")
    public List<assignee> assignManagerToAssignee(
            @PathVariable("idmanager") Long idManager,
            @RequestBody List<assignee> assignees) {
        return userService.affectmultipleassignees(assignees,idManager);
    }

    @GetMapping("/assignnebymanager/{name}")
    public List<assignee> getassigneesbymanager(@PathVariable String name ) {

        return assigneeRepository.findByManagerName(name) ;
    }

    @PutMapping("/assigne-director-to-manager/{iddirector}/{idmanager}")
    public User assigndirectortoamanager(
            @PathVariable("idmanager") Long idmanager,
            @PathVariable("iddirector") Long iddirector)
            {
        User d = userRepository.findById(iddirector).orElse(null);
        User m = userRepository.findById(idmanager).orElse(null);
        m.setDirector(d);
        return userRepository.save(m);
    }

    @GetMapping("/managerbydirector/{iddirector}")
    public List<User> getmanagerbydirector(@PathVariable Long iddirector ) {

        return userRepository.findByDirectorId(iddirector) ;
    }


}
