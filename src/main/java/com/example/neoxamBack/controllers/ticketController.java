package com.example.neoxamBack.controllers;

import com.example.neoxamBack.entities.*;
import com.example.neoxamBack.repositories.AssigneeModuleStatusRepository;
import com.example.neoxamBack.repositories.assigneeRepository;
import com.example.neoxamBack.repositories.moduleRepository;
import com.example.neoxamBack.repositories.ticketrepository;
import com.example.neoxamBack.services.ExcelService;
import com.example.neoxamBack.services.IModuleService;
import com.example.neoxamBack.services.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/tickets")
@CrossOrigin("*")
public class ticketController {
    ITicketService ticketService;
    moduleRepository moduleRepository;
    IModuleService moduleService;
    @Autowired
    ticketrepository ticketRepository;
    assigneeRepository assigneeRepository;
    AssigneeModuleStatusRepository assigneeModuleStatusRepository;


    @GetMapping("/alltickets")
    public List<ticket> getalltickets() {
        return ticketService.getalltickets();
    }

    @GetMapping("/allassignees")
    public List<assignee> getallassignees() {
        return assigneeRepository.findAll();
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
    @PutMapping("/update-module-status")
    public ResponseEntity<String> updateAllAssigneeModuleStatuses() {
        moduleService.updateAllAssigneeModuleStatus();
        return ResponseEntity.ok("Module statuses updated for all assignees.");
    }

    @GetMapping("/assignee/modules/{assigneeName}")
    public List<AssigneeModuleStatusDto> getModulesWithAssigneeStatusByAssigneeName(@PathVariable String assigneeName) {
        return moduleService.getModulesWithAssigneeStatusByAssigneeName(assigneeName);
    }

    @PutMapping("/updatestatus/{idmodule}/{assigneename}")
    public AssigneeModuleStatus updateassigneestatus(@PathVariable String assigneename,@PathVariable int idmodule) {
        return moduleService.updateassigneestatus(assigneename,idmodule);
    }

    @PostMapping("/updateversion")
    public void updateversion(){
        ticketService.updateversion();
    }

    @GetMapping("/tickets/version/{version}")
    public List<ticket> getticketbyversion(@PathVariable int version) {
        return ticketService.findticketsbyversion(version);
    }
    @GetMapping("/tickets/allversions")
    public List<Integer> getallticketsversions() {
        return ticketService.findticketversions();
    }

    @DeleteMapping("/version/{version}")
    public ResponseEntity<String> deleteTicketsByVersion(@PathVariable int version) {
        try {
            ticketService.deleteTicketsByVersion(version);
            return ResponseEntity.ok("Tickets deleted successfully for version: " + version);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while deleting tickets: " + e.getMessage());
        }
    }

    @GetMapping("/modulebystatus/{name}")
    public List<AssigneeModuleStatus> getallticketsversions(@PathVariable String name) {
        return assigneeModuleStatusRepository.findByModule_Name(name);
    }

    @GetMapping("/count-by-assignee/forManager/{idmanager}")
    public ResponseEntity<List<Map<String, Object>>> getTicketCountsByAssigneeAndModuleforManagaer(@PathVariable int idmanager) {
        List<Object[]> results = ticketService.countTicketsByAssigneeAndModuleforManagaer(idmanager);
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

    @PutMapping("/update-module-ticketrequired/{idmodule}/{nbtickets}")
    public module updatemoduleticketrequired(@PathVariable int idmodule,@PathVariable int nbtickets) {
        module m = moduleRepository.findById(idmodule).orElse(null);
        m.setTicketrequired(nbtickets);
        return moduleRepository.save(m);
    }

    @GetMapping("/getlistmodules")
    public List<module> getlistmodules() {
        return moduleRepository.findAll();
    }




}
