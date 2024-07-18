package com.example.neoxamBack.repositories;
import com.example.neoxamBack.entities.ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ticketrepository extends JpaRepository<ticket,Integer>{
    @Query("SELECT t.feature.module.name, t.assignee.name, COUNT(t.id) FROM ticket t GROUP BY t.feature.module.name, t.assignee.name")
    List<Object[]> countTicketsByAssigneeAndModule();



    @Query("SELECT t FROM ticket t " +
            "JOIN FETCH t.feature f " +
            "JOIN FETCH f.module m " +
            "WHERE m.name = :moduleName")
    List<ticket> findTicketsByModuleName(@Param("moduleName") String moduleName);
    @Query("SELECT m.name as module, f.name as feature, a.name as assignee, COUNT(t.id) as workCount " +
            "FROM ticket t " +
            "JOIN t.feature f " +
            "JOIN f.module m " +
            "JOIN t.assignee a " +
            "WHERE m.name = :moduleName " +
            "GROUP BY m.name, f.name, a.name")
    List<Object[]> findAssigneeWorkCountsByModuleName(@Param("moduleName") String moduleName);

    @Query("SELECT m.name as module, f.name as feature, COUNT(t.id) as workCount " +
            "FROM ticket t " +
            "JOIN t.feature f " +
            "JOIN f.module m " +
            "JOIN t.assignee a " +
            "WHERE a.name = :assigneeName " +
            "GROUP BY m.name, f.name")
    List<Object[]> findModulesAndFeaturesByAssigneeName(@Param("assigneeName") String assigneeName);

    @Query("SELECT a.name as assignee, COUNT(DISTINCT m.id) as modulesWorked " +
            "FROM ticket t " +
            "JOIN t.feature f " +
            "JOIN f.module m " +
            "JOIN t.assignee a " +
            "GROUP BY a.name")
    List<Object[]> findAssigneeModuleCounts();

    @Query("SELECT a.name as assignee, COUNT(DISTINCT m.id) as moduleCount " +
            "FROM ticket t " +
            "JOIN t.assignee a " +
            "JOIN t.feature f " +
            "JOIN f.module m " +
            "GROUP BY a.name")
    List<Object[]> countModulesByAssignee();


    @Query("SELECT a.name as assignee, COUNT(DISTINCT f.id) as featureCount " +
            "FROM ticket t " +
            "JOIN t.assignee a " +
            "JOIN t.feature f " +
            "GROUP BY a.name")
    List<Object[]> countFeaturesByAssignee();


    @Query("SELECT a.name as assignee, COUNT(t.id) as ticketCount " +
            "FROM ticket t " +
            "JOIN t.assignee a " +
            "GROUP BY a.name")
    List<Object[]> countTicketsByAssignee();
    @Query("SELECT DISTINCT m.name FROM ticket t JOIN t.feature f JOIN f.module m")
    List<String> findDistinctModuleNames();

}
