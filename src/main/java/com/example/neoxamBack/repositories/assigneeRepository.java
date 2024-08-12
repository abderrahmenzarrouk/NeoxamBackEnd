package com.example.neoxamBack.repositories;

import com.example.neoxamBack.entities.assignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface assigneeRepository extends JpaRepository<assignee,Integer> {
    Optional<assignee> findByName(String name);

    @Query("SELECT a FROM assignee a WHERE a.manager.email = :name OR a.manager.lastName = :name")
    List<assignee> findByManagerName(@Param("name") String name);
}
