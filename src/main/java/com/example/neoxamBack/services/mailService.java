package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.repositories.userRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.neoxamBack.entities.mail;

import java.util.Random;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class mailService {
    @Autowired
    userRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Value("$(spring.mail.username)")
    private String fromMail;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public void sendEmail(String mail){
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject("Forgot your password");
        simpleMailMessage.setText("New password : "+password);
        simpleMailMessage.setTo(mail);
        User u = userRepository.findByEmail(mail).get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedpassword = passwordEncoder.encode(password.toString());
        u.setPassword(encodedpassword);
        userRepository.save(u);
        mailSender.send(simpleMailMessage);

    }
    public void sendEmailaccept(String mail){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject("Approval");
        simpleMailMessage.setText("Your account has been approved , You can login now");
        simpleMailMessage.setTo(mail);
        mailSender.send(simpleMailMessage);
    }
    public void sendEmailban(String mail){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject("Ban");
        simpleMailMessage.setText("Your account has been Banned , You can't login anymore");
        simpleMailMessage.setTo(mail);
        mailSender.send(simpleMailMessage);
    }
}
