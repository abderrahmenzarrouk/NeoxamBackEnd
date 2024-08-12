package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.entities.assignee;
import com.example.neoxamBack.repositories.assigneeRepository;
import com.example.neoxamBack.repositories.userRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class userService implements UserDetailsService {
    private final userRepository userRepository;
    private final assigneeRepository assigneeRepository;
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
    public String resetpassword(Long id,String oldpassword, String newpassword){
        User u = userRepository.findById(id).get();
        String password = u.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(oldpassword, password)){
            String encodedrandompassword = passwordEncoder.encode(newpassword);
            u.setPassword(encodedrandompassword);
            userRepository.save(u);
            return "Password changed with success";
        }else {
            throw new IllegalArgumentException("Incorrect old password");
        }
    }

    public List<User> getallusers(){
        return userRepository.findAll();
    }
    public User acceptuser(String email){
        User u = userRepository.findByEmail(email).get();
        u.setEnabled(true);
        userRepository.save(u);
        return u;
    }
    public User banuser(String email){
        User u = userRepository.findByEmail(email).get();
        u.setEnabled(false);
        userRepository.save(u);
        return u;
    }

    public assignee affectmanagertoassignee(Long idmanager,int idassignee){
        User manager = userRepository.findById(idmanager).orElse(null);
        assignee assignee = assigneeRepository.findById(idassignee).orElse(null);
        assignee.setManager(manager);
        assigneeRepository.save(assignee);
        return assignee;
    }
    public List<assignee> affectmultipleassignees(List<assignee> listA, Long idmanager ){
        User manager = userRepository.findById(idmanager).orElse(null);

        listA.forEach(assignee -> {

            assignee existingAssignee = assigneeRepository.findByName(assignee.getName()).get();

            if (existingAssignee != null) {

                existingAssignee.setManager(manager);
                assigneeRepository.save(existingAssignee);
            } else {

                System.out.println("Assignee with name " + assignee.getName() + " not found.");
            }
        });

        return listA;

    }

}
