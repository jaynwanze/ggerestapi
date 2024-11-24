package com.example.ggerestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String createEmission(@RequestParam String category, @RequestParam String description,
            @RequestParam int year,
            @RequestParam float value, @RequestParam float predictedValue, @RequestParam String scenario,
            @RequestParam String gasUnits, @RequestParam String nk, Model model) {
        Emission emission = new Emission();
        emission.setCategory(category);
        emission.setCategoryDescription(description);
        emission.setScenario(scenario);
        emission.setValue(value);
        emission.setYear(year);
        emission.setGasUnits(gasUnits);
        emission.setNk(nk);
        emission.setPredictedValue(predictedValue);
        emission.setCountry("Ireland");
        emissionRepository.save(emission);
        model.addAttribute("success", "Emission created successfully.");
        return "redirect:/emissions";

    }

    @PutMapping(value = "/emissions/{id}")
    public String updateEmission(Long id, @RequestParam String category, @RequestParam String description,
            @RequestParam int year,
            @RequestParam float value, @RequestParam float predictedValue, @RequestParam String scenario,
            @RequestParam String gasUnits, @RequestParam String nk, Model model) {
        Emission emission = emissionRepository.findById(id).orElse(null);
        if (emission == null) {
            model.addAttribute("error", "Emission not found.");
            return "redirect:/emissions";
        }
        emission.setCategory(category);
        emission.setCategoryDescription(description);
        emission.setScenario(scenario);
        emission.setValue(value);
        emission.setYear(year);
        emission.setGasUnits(gasUnits);
        emission.setNk(nk);
        emission.setPredictedValue(predictedValue);
        emissionRepository.save(emission);
        model.addAttribute("success", "Emission updated successfully.");
        return "redirect:/emissions";
    }

   @DeleteMapping(value = "/emissions/{id}")
public String deleteEmission(@PathVariable Long id, Model model) {
    Emission existingEmission = emissionRepository.findById(id).orElse(null);
    if (existingEmission == null) {
        model.addAttribute("error", "Emission not found.");
        return "redirect:/emissions";
    }
    emissionRepository.deleteById(id);
    model.addAttribute("success", "Emission deleted successfully.");
    return "redirect:/emissions";
}

    @PostMapping(value = "/emissions/populate")
    public String populateEmissions(Model model) {
        List<Emission> emissions = EmissionParser.parseEmissions();
        emissionRepository.saveAll(emissions);
        model.addAttribute("success", "Emissions populated successfully.");
        return "redirect:/emissions";
    }
}
