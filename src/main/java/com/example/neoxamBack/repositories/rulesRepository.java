package com.example.neoxamBack.repositories;

import com.example.neoxamBack.entities.rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface rulesRepository extends JpaRepository<rules,Long> {
}
