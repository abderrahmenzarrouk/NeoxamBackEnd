package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.repositories.userRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class userService implements UserDetailsService {
    private final userRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String user_not_found_MSG = "user with email %s is absent ";
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(String.format(user_not_found_MSG, email)));
    }
    public String signUpUser(User user){
        System.out.println("user");
        System.out.println(user.getLastName());
        boolean userExists =  userRepository.findByEmail(user.getEmail()).isPresent();
        if(userExists){
            throw new IllegalStateException("user exists ");
        }
        System.out.println("user password"+user.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedpassword = passwordEncoder.encode(user.getPassword());
        // String encodedpassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedpassword);
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        return token;
    }

}
