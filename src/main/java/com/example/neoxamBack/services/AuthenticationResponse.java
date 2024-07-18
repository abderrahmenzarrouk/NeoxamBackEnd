package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.User;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private User user;
}
