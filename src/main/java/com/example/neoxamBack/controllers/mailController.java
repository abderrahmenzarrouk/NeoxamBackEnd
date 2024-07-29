package com.example.neoxamBack.controllers;

import com.example.neoxamBack.services.mailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.neoxamBack.entities.mail;

@RestController
@RequestMapping("api/v1/auth/mail/")
@CrossOrigin("*")
public class mailController {
    @Autowired
    private mailService mailService;
    @PutMapping("send/{mail}")
    public String sendemail(@PathVariable String mail){
        System.out.println("sending ...");
        System.out.println(mail);
        mailService.sendEmail(mail);
        return "mail sended with success";
    }
    @PutMapping("sendaccept/{mail}")
    public String sendemailaccept(@PathVariable String mail){
        mailService.sendEmailaccept(mail);
        return "mail sended with success";
    }
}
