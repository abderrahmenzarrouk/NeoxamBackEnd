package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.AssigneeModuleStatus;
import com.example.neoxamBack.entities.AssigneeModuleStatusDto;
import com.example.neoxamBack.repositories.AssigneeModuleStatusRepository;
import com.example.neoxamBack.repositories.assigneeRepository;
import com.example.neoxamBack.repositories.moduleRepository;
import com.example.neoxamBack.repositories.ticketrepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.neoxamBack.entities.module;
import com.example.neoxamBack.entities.assignee;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModuleService implements IModuleService{
    moduleRepository moduleRepository;
    ticketrepository ticketRepository;
    assigneeRepository assigneeRepository;
    AssigneeModuleStatusRepository assigneeModuleStatusRepository;
    @Override
    public void updateAllAssigneeModuleStatus() {
        List<module> allModules = moduleRepository.findAll();
        List<assignee> allAssignees = assigneeRepository.findAll();

        for (assignee assignee : allAssignees) {
            for (module module : allModules) {
                long ticketCount = ticketRepository.countByAssigneeIdAndFeatureModuleId(assignee.getId(), module.getId());

                AssigneeModuleStatus assigneeModuleStatus = assigneeModuleStatusRepository
                        .findByAssigneeAndModule(assignee, module)
                        .orElse(new AssigneeModuleStatus());

                assigneeModuleStatus.setAssignee(assignee);
                assigneeModuleStatus.setModule(module);

                assigneeModuleStatus.setAssigneeStatus(ticketCount > 0 ? 2 : 0);

                assigneeModuleStatusRepository.save(assigneeModuleStatus);
            }
        }
    }
    @Override
    public List<module> getModulesByAssigneeId(Integer assigneeId) {
        return moduleRepository.findModulesByAssigneeId(assigneeId);
    }

    @Override
    public List<AssigneeModuleStatusDto> getModulesWithAssigneeStatusByAssigneeName(String assigneeName) {
        List<Object[]> rawResults = assigneeModuleStatusRepository.findModulesWithAssigneeStatusByAssigneeName(assigneeName);


        List<AssigneeModuleStatusDto> resultDtos = rawResults.stream()
                .map(result -> {
                    module module = (module) result[0];
                    int status = (int) result[1];

                    // Create DTO object
                    return new AssigneeModuleStatusDto(module, status);
                })
                .collect(Collectors.toList());

        return resultDtos;
    }

    @Override
    public AssigneeModuleStatus updateassigneestatus(String assigneename,int idmodule) {
        System.out.println("here");
        AssigneeModuleStatus AMS = assigneeModuleStatusRepository.findByAssignee_NameAndModule_Id(assigneename,idmodule);
        System.out.println(AMS);
        AMS.setAssigneeStatus(1);
        assigneeModuleStatusRepository.save(AMS);
        return  AMS;
    }

    @Override
    public List<AssigneeModuleStatus> findbystatus(String name) {
        return assigneeModuleStatusRepository.findByModule_Name(name);
    }


}
