package com.example.ggerestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ggerestapi.entity.Emission;
import com.example.ggerestapi.parser.EmissionParser;
import com.example.ggerestapi.repository.EmissionRepository;
import com.example.ggerestapi.service.EmissionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RequestMapping("/api")
@Controller
public class EmissionController {

    @Autowired
    private EmissionService emissionService;

    @PostMapping(value = "/emissions")
    public String createEmission(@RequestParam String category, @RequestParam String categoryDescription,
            @RequestParam int year,
            @RequestParam float value, @RequestParam float predictedValue, @RequestParam String scenario,
            @RequestParam String gasUnits, @RequestParam String nk, RedirectAttributes redirectAttributes) {
        return emissionService.createEmission(category, categoryDescription, year, value, predictedValue, scenario,
                gasUnits, nk, redirectAttributes);
    }

    @PutMapping(value = "/emissions/{id}")
    public String updateEmission(@PathVariable Long id, @RequestParam String category,
            @RequestParam String categoryDescription,
            @RequestParam int year,
            @RequestParam float value, @RequestParam float predictedValue, @RequestParam String scenario,
            @RequestParam String gasUnits, @RequestParam String nk, RedirectAttributes redirectAttributes) {
        return emissionService.updateEmission(id, category, categoryDescription, year, value, predictedValue, scenario,
                gasUnits, nk, redirectAttributes);
    }

    @DeleteMapping(value = "/emissions/{id}")
    public String deleteEmission(@PathVariable Long id, RedirectAttributes redirectAttributes) {
       return emissionService.deleteEmission(id, redirectAttributes);
    }

    @DeleteMapping(value = "/emissions")
    public String deleteAllEmissions(RedirectAttributes redirectAttributes) {
        return emissionService.deleteAllEmissions(redirectAttributes);
    }

    @PostMapping(value = "/emissions/populate")
    public String populateEmissions(RedirectAttributes redirectAttributes) {
        return emissionService.populateEmissions(redirectAttributes);
    }
}
