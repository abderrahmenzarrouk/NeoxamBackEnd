package com.example.neoxamBack.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "assignee_module_status")
public class AssigneeModuleStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private assignee assignee;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private module module;

    private int assigneeStatus;
}

