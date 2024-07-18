package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.rules;
import com.example.neoxamBack.repositories.rulesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RulesService implements IRulesService{
    private rulesRepository rulesRepository;

    @Override
    public rules addrule(rules rule) {
       return rulesRepository.save(rule);
    }

    @Override
    public List<rules> getallrules() {
        return rulesRepository.findAll();
    }
}
