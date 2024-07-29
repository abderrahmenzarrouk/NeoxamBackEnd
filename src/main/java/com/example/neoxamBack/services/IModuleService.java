package com.example.neoxamBack.services;

import java.util.List;

import com.example.neoxamBack.entities.AssigneeModuleStatus;
import com.example.neoxamBack.entities.AssigneeModuleStatusDto;
import com.example.neoxamBack.entities.assignee;
import com.example.neoxamBack.entities.module;

public interface IModuleService {
    void updateAllAssigneeModuleStatus();
    List<module> getModulesByAssigneeId(Integer assigneeId);
    List<AssigneeModuleStatusDto> getModulesWithAssigneeStatusByAssigneeName(String assigneeName);
    AssigneeModuleStatus updateassigneestatus(String assigneename,int idmodule);

    List<AssigneeModuleStatus> findbystatus(String name);
}
