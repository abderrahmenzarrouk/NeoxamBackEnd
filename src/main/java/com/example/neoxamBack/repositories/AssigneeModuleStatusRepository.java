package com.example.neoxamBack.repositories;

import com.example.neoxamBack.entities.AssigneeModuleStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.neoxamBack.entities.module;
import com.example.neoxamBack.entities.assignee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssigneeModuleStatusRepository extends JpaRepository<AssigneeModuleStatus, Integer> {

    Optional<AssigneeModuleStatus> findByAssigneeAndModule(assignee assignee, module module);
    @Query("SELECT ams.module, ams.assigneeStatus FROM AssigneeModuleStatus ams WHERE ams.assignee.name = :assigneeName")
    List<Object[]> findModulesWithAssigneeStatusByAssigneeName(@Param("assigneeName") String assigneeName);

    AssigneeModuleStatus findByAssignee_NameAndModule_Id(String assigneename, int idmodule);

    List<AssigneeModuleStatus> findByModule_Name(String modulename);
}
