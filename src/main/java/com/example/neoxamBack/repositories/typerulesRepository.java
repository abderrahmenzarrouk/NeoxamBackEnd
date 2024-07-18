package com.example.neoxamBack.repositories;

import com.example.neoxamBack.entities.TypeRules;
import com.example.neoxamBack.entities.User;
import com.example.neoxamBack.entities.typerule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface typerulesRepository extends JpaRepository<TypeRules,Long> {
    List<TypeRules> findByType (typerule type);
}
