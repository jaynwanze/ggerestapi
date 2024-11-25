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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RequestMapping("/api")
@Controller
public class EmissionController {

    @Autowired
    private EmissionRepository emissionRepository;

    @PostMapping(value = "/emissions")
    public String createEmission(@RequestParam String category, @RequestParam String categoryDescription,
            @RequestParam int year,
            @RequestParam float value, @RequestParam float predictedValue, @RequestParam String scenario,
            @RequestParam String gasUnits, @RequestParam String nk, RedirectAttributes redirectAttributes) {
        Emission emission = new Emission();
        emission.setCategory(category);
        emission.setCategoryDescription(categoryDescription);
        emission.setScenario(scenario);
        emission.setValue(value);
        emission.setYear(year);
        emission.setGasUnits(gasUnits);
        emission.setNk(nk);
        emission.setPredictedValue(predictedValue);
        emission.setCountry("Ireland");
        emissionRepository.save(emission);
        redirectAttributes.addFlashAttribute("success", "Emission created successfully.");
        return "redirect:/emissions";

    }

    @PutMapping(value = "/emissions/{id}")
    public String updateEmission(@PathVariable Long id, @RequestParam String category,
            @RequestParam String categoryDescription,
            @RequestParam int year,
            @RequestParam float value, @RequestParam float predictedValue, @RequestParam String scenario,
            @RequestParam String gasUnits, @RequestParam String nk, RedirectAttributes redirectAttributes) {
        Emission emission = emissionRepository.findById(id).orElse(null);
        if (emission == null) {
            redirectAttributes.addFlashAttribute("error", "Emission not found.");
            return "redirect:/emissions";
        }
        emission.setCategory(category);
        emission.setCategoryDescription(categoryDescription);
        emission.setScenario(scenario);
        emission.setValue(value);
        emission.setYear(year);
        emission.setGasUnits(gasUnits);
        emission.setNk(nk);
        emission.setPredictedValue(predictedValue);
        emissionRepository.save(emission);
        redirectAttributes.addFlashAttribute("success", "Emission updated successfully.");
        return "redirect:/emissions";
    }

    @DeleteMapping(value = "/emissions/{id}")
    public String deleteEmission(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Emission existingEmission = emissionRepository.findById(id).orElse(null);
        if (existingEmission == null) {
            redirectAttributes.addFlashAttribute("error", "Emission not found.");
            return "redirect:/emissions";
        }
        emissionRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Emission deleted successfully.");
        return "redirect:/emissions";
    }

    @PostMapping(value = "/emissions/populate")
    public String populateEmissions(RedirectAttributes redirectAttributes) {
        List<Emission> emissions = EmissionParser.parseEmissions();
        if (emissions == null) {
            redirectAttributes.addFlashAttribute("error", "Error populating emissions.");
            return "redirect:/emissions";
        }
        emissionRepository.saveAll(emissions);
        redirectAttributes.addFlashAttribute("success", "Emissions populated successfully.");
        return "redirect:/emissions";
    }
}
