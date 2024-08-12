package com.example.neoxamBack.controllers;

import com.example.neoxamBack.entities.assignee;
import com.example.neoxamBack.repositories.assigneeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin("*")
public class AssigneeController {
    @Autowired
    private assigneeRepository assigneeRepository;

    @PutMapping("/set-assignee-rank/{idassignee}/{rank}")
    public assignee setRank(@PathVariable int idassignee, @PathVariable String rank) {
        assignee a = assigneeRepository.findById(idassignee).orElse(null);
        a.setRank(rank);
        assigneeRepository.save(a);
        return a;

    }
}
