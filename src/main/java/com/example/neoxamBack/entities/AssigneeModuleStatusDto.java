package com.example.neoxamBack.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssigneeModuleStatusDto {
    private module module;
    private int status;
}