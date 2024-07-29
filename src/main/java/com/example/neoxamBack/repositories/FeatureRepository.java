package com.example.neoxamBack.repositories;

import com.example.neoxamBack.entities.feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<feature, Integer> {
    feature findByNameAndModuleId(String name, Integer moduleId);
}