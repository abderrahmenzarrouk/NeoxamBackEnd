package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.rules;

import java.util.List;

public interface IRulesService {
    rules addrule(rules rule);
    List<rules> getallrules();
}
