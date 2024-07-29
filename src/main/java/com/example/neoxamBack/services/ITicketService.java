package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.ticket;

import java.util.List;
import java.util.Map;

public interface ITicketService {
    List<ticket> getalltickets();
    ticket getticketbyid(int id);
    List<Object[]> getTicketCountsByAssigneeAndModule();
    List<Map<String, Object>> findAssigneeWorkCountsByModuleName(String moduleName);
    List<Map<String, Object>> findModulesAndFeaturesByAssigneeName(String assigneeName);
    List<Map<String, Object>> getAssigneeModuleCounts();
    List<ticket> findTicketsByModuleName(String moduleName);
    List<Map<String, Object>> getAssigneeStats();
    List<String> getallmodules();
    void updateversion();
    List<ticket> findticketsbyversion(int version);
    List<Integer> findticketversions();
    void deleteTicketsByVersion(int version);

}
