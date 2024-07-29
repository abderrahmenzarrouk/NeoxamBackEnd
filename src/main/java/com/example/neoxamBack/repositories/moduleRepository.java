package com.example.neoxamBack.repositories;
import com.example.neoxamBack.entities.module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface moduleRepository extends JpaRepository<module,Integer> {
    @Query("SELECT m FROM module m WHERE m.id IN (SELECT DISTINCT t.feature.module.id FROM ticket t WHERE t.assignee.id = :assigneeId)")
    List<module> findModulesByAssigneeId(Integer assigneeId);

    Optional<module> findByName(String name);

}
