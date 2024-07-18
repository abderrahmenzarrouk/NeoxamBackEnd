package com.example.neoxamBack.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "ticket")
public class ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    private String priority;
    private String status;
    private String Reporter;
    private Date date_created;
    private String summary;
    private String resolution;
    private Date date_resolved;
    @ManyToOne
    private feature feature;
    @ManyToOne
    private assignee assignee;
}
