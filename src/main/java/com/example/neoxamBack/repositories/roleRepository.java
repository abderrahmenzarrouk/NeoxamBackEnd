package com.example.neoxamBack.repositories;

import com.example.neoxamBack.entities.Role;
import com.example.neoxamBack.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface roleRepository extends JpaRepository<Roles, Integer> {
    Roles findByRole(Role r);
}