package com.example.neoxamBack.controllers;

import com.example.neoxamBack.entities.TypeRules;
import com.example.neoxamBack.entities.rules;
import com.example.neoxamBack.entities.typerule;
import com.example.neoxamBack.repositories.rulesRepository;
import com.example.neoxamBack.repositories.typerulesRepository;
import com.example.neoxamBack.services.IRulesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "rules")
@AllArgsConstructor
@CrossOrigin("*")
public class rulesController {
    IRulesService rulesService;
    private typerulesRepository typeRulesRepository;
    private rulesRepository rulesRepository;

    @PostMapping("/addrule")
    public rules addrule(@RequestBody rules rule) {
        return rulesService.addrule(rule);
    }

    @GetMapping("/allrules")
    public List<rules> getalltickets() {
        return rulesService.getallrules();
    }

    @GetMapping("/rolebytype")
    public List<TypeRules> findByType(@RequestParam("type") typerule type) {
        return typeRulesRepository.findByType(type);
    }
    @GetMapping("/allrulestypes")
    public List<TypeRules> findrulestypes() {
        return typeRulesRepository.findAll();
    }

    @DeleteMapping("/deleterule")
    public void deleterule(@RequestParam("id") Long id) {
        rulesRepository.deleteById(id);
    }
}
