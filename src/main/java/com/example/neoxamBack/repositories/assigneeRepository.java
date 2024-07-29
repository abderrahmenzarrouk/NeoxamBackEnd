package com.example.neoxamBack.repositories;

import com.example.neoxamBack.entities.assignee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface assigneeRepository extends JpaRepository<assignee,Integer> {
    Optional<assignee> findByName(String name);
}
