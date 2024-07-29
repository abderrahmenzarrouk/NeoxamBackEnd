package com.example.neoxamBack.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class changepasswordRequest {
    private String oldpassword;
    private String newpassword;
}
