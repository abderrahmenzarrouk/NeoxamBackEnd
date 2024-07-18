package com.example.neoxamBack.controllers;

import com.example.neoxamBack.repositories.moduleRepository;
import com.example.neoxamBack.repositories.ticketrepository;
import com.example.neoxamBack.services.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.neoxamBack.entities.ticket;
import com.example.neoxamBack.entities.module;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/tickets")
@CrossOrigin("*")
public class ticketController {
    ITicketService ticketService;
    moduleRepository moduleRepository;
    @Autowired
    ticketrepository ticketRepository;
    @GetMapping("/alltickets")
    public List<ticket> getalltickets() {
        return ticketService.getalltickets();
    }
    @GetMapping("/ticketbyid/{id}")
    public ticket getticketbyid(@PathVariable("id") int id) {
        return ticketService.getticketbyid(id);
    }

    @GetMapping("/count-by-assignee")
    public ResponseEntity<List<Map<String, Object>>> getTicketCountsByAssigneeAndModule() {
        List<Object[]> results = ticketService.getTicketCountsByAssigneeAndModule();
        Map<String, Map<String, Integer>> moduleAssigneeMap = new HashMap<>();

        for (Object[] result : results) {
            String moduleName = (String) result[0];
            String assigneeName = (String) result[1];
            Long count = (Long) result[2];

            moduleAssigneeMap.computeIfAbsent(moduleName, k -> new HashMap<>()).put(assigneeName, count.intValue());
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> entry : moduleAssigneeMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("module", entry.getKey());
            map.put("assignees", entry.getValue());
            response.add(map);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-module/{moduleName}")
    public ResponseEntity<List<Map<String, Object>>> getTicketsByModuleName(@PathVariable String moduleName) {
        List<Map<String, Object>> results = ticketService.findAssigneeWorkCountsByModuleName(moduleName);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/modules/{assigneeName}")
    public ResponseEntity<List<Map<String, Object>>> getModulesAndFeaturesByAssignee(@PathVariable String assigneeName) {
        List<Map<String, Object>> results = ticketService.findModulesAndFeaturesByAssigneeName(assigneeName);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/module-counts")
    public ResponseEntity<List<Map<String, Object>>> getAssigneeModuleCounts() {
        List<Map<String, Object>> results = ticketService.getAssigneeModuleCounts();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/tickets-by-module/{moduleName}")
    public ResponseEntity<List<ticket>> getTickets(@PathVariable String moduleName) {
        List<ticket> tickets = ticketService.findTicketsByModuleName(moduleName);
        System.out.println("total: " + tickets.size());
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/assigneeoverallstats")
    public ResponseEntity<List<Map<String, Object>>> getAssigneeStats() {
        List<Map<String, Object>> stats = ticketService.getAssigneeStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/allmodules")
    public List<String> getallmodules() {
        return ticketService.getallmodules();
    }

}
