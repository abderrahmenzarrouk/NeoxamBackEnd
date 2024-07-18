package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.ticket;
import com.example.neoxamBack.repositories.moduleRepository;
import com.example.neoxamBack.repositories.ticketrepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService implements ITicketService {
    @Autowired
    ticketrepository ticketrepository;

    @Autowired
    moduleRepository moduleRepository;


    @Override
    public List<ticket> getalltickets() {
        return ticketrepository.findAll();
    }

    @Override
    public ticket getticketbyid(int id) {
        return ticketrepository.findById(id).get();
    }

    @Override
    public List<Object[]> getTicketCountsByAssigneeAndModule() {
        return ticketrepository.countTicketsByAssigneeAndModule();
    }


    public List<Map<String, Object>> findAssigneeWorkCountsByModuleName(String moduleName) {
        List<Object[]> results = ticketrepository.findAssigneeWorkCountsByModuleName(moduleName);

        Map<String, Map<String, List<Map<String, Object>>>> featureMap = new HashMap<>();

        for (Object[] result : results) {

            String feature = (String) result[1];
            String assignee = (String) result[2];
            Long workCount = (Long) result[3];


            featureMap.putIfAbsent(feature, new HashMap<>());
            featureMap.get(feature).putIfAbsent("assignees", new ArrayList<>());

            Map<String, Object> assigneeWork = new HashMap<>();
            assigneeWork.put("assignee", assignee);
            assigneeWork.put("workCount", workCount);



            featureMap.get(feature).get("assignees").add(assigneeWork);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (String feature : featureMap.keySet()) {
            Map<String, Object> featureAssignees = new HashMap<>();
            featureAssignees.put("feature", feature);
            featureAssignees.put("assignees", featureMap.get(feature).get("assignees"));

            response.add(featureAssignees);
        }

        return response;
    }

    public List<Map<String, Object>> findModulesAndFeaturesByAssigneeName(String assigneeName) {
        List<Object[]> results = ticketrepository.findModulesAndFeaturesByAssigneeName(assigneeName);
        Map<String, Map<String, List<Map<String, Object>>>> moduleMap = new HashMap<>();

        for (Object[] result : results) {
            String module = (String) result[0];
            String feature = (String) result[1];
            Long workCount = (Long) result[2];

            moduleMap.putIfAbsent(module, new HashMap<>());
            moduleMap.get(module).putIfAbsent("features", new ArrayList<>());

            Map<String, Object> featureWork = new HashMap<>();
            featureWork.put("feature", feature);
            featureWork.put("workCount", workCount);

            moduleMap.get(module).get("features").add(featureWork);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (String module : moduleMap.keySet()) {
            Map<String, Object> moduleFeatures = new HashMap<>();
            moduleFeatures.put("module", module);
            moduleFeatures.put("features", moduleMap.get(module).get("features"));
            response.add(moduleFeatures);
        }

        return response;
    }

    public List<Map<String, Object>> getAssigneeModuleCounts() {
        List<Object[]> results = ticketrepository.findAssigneeModuleCounts();
        List<Map<String, Object>> response = new ArrayList<>();

        // Calculate total number of modules
        long totalModules = moduleRepository.count();

        for (Object[] result : results) {
            String assignee = (String) result[0];
            Long modulesWorked = (Long) result[1];

            Map<String, Object> assigneeModules = new HashMap<>();
            assigneeModules.put("assignee", assignee);
            assigneeModules.put("modulesWorked", modulesWorked);
            assigneeModules.put("moduleRatio", ((double) modulesWorked / totalModules)*100) ;

            response.add(assigneeModules);
        }

        return response;
    }

    public List<ticket> findTicketsByModuleName(String moduleName) {
        return ticketrepository.findTicketsByModuleName(moduleName);
    }

    public List<Map<String, Object>> getAssigneeStats() {
        List<Object[]> moduleCounts = ticketrepository.countModulesByAssignee();
        List<Object[]> featureCounts = ticketrepository.countFeaturesByAssignee();
        List<Object[]> ticketCounts = ticketrepository.countTicketsByAssignee();


        Map<String, Object[]> moduleMap = moduleCounts.stream()
                .collect(Collectors.toMap(obj -> (String) obj[0], obj -> obj));

        Map<String, Object[]> featureMap = featureCounts.stream()
                .collect(Collectors.toMap(obj -> (String) obj[0], obj -> obj));

        Map<String, Object[]> ticketMap = ticketCounts.stream()
                .collect(Collectors.toMap(obj -> (String) obj[0], obj -> obj));


        return moduleMap.keySet().stream().map(assignee -> {
            Map<String, Object> result = new HashMap<>();
            result.put("assignee", assignee);

            Object[] moduleCount = moduleMap.getOrDefault(assignee, new Object[]{assignee, 0L});
            result.put("moduleCount", moduleCount[1]);

            Object[] featureCount = featureMap.getOrDefault(assignee, new Object[]{assignee, 0L});
            result.put("featureCount", featureCount[1]);

            Object[] ticketCount = ticketMap.getOrDefault(assignee, new Object[]{assignee, 0L});
            result.put("ticketCount", ticketCount[1]);

            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getallmodules() {
        return ticketrepository.findDistinctModuleNames();
    }
}
