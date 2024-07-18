package com.example.neoxamBack.entities;

import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles userRole;
    private boolean locked = false;
    private boolean enabled = true;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.getRole().name()));
    }

    public User(String lastname, String firstName, String email, String password, Roles userRole) {
        this.lastName = lastname;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


}
