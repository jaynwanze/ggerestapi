package com.example.ggerestapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ggerestapi.entity.Emission;
import com.example.ggerestapi.parser.EmissionParser;
import com.example.ggerestapi.repository.EmissionRepository;

@Service
public class EmissionService {

    @Autowired
    private EmissionRepository emissionRepository;

    public String createEmission(@RequestParam String category, @RequestParam String categoryDescription,
            @RequestParam int year,
            @RequestParam float value, @RequestParam float predictedValue, @RequestParam String scenario,
            @RequestParam String gasUnits, @RequestParam String nk, RedirectAttributes redirectAttributes) {
        Emission emission = new Emission();
        emission.setCategory(category);
        emission.setCategoryDescription(
                categoryDescription.isEmpty() || categoryDescription == null ? null : categoryDescription);
        emission.setScenario(scenario);
        emission.setValue(value);
        emission.setYear(year);
        emission.setGasUnits(gasUnits);
        emission.setNk(nk.isEmpty() || nk == null ? null : nk);
        emission.setPredictedValue(predictedValue);
        emission.setCountry("Ireland");
        emissionRepository.save(emission);
        redirectAttributes.addFlashAttribute("success", "Emission created successfully.");
        return "redirect:/emissions";

    }

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
        emission.setCategoryDescription(
                categoryDescription.isEmpty() || categoryDescription == null ? null : categoryDescription);
        emission.setScenario(scenario);
        emission.setValue(value);
        emission.setYear(year);
        emission.setGasUnits(gasUnits);
        emission.setNk(nk.isEmpty() || nk == null ? null : nk);
        emission.setPredictedValue(predictedValue);
        emissionRepository.save(emission);
        redirectAttributes.addFlashAttribute("success", "Emission updated successfully.");
        return "redirect:/emissions";
    }

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

    public String populateEmissions(RedirectAttributes redirectAttributes) {
        List<Emission> exisitngEmissions = emissionRepository.findAll();
        if (exisitngEmissions.size() >= 242) {
            redirectAttributes.addFlashAttribute("error", "Emissions already populated.");
            return "redirect:/emissions";
        }
        List<Emission> emissions = EmissionParser.parseEmissions();
        if (emissions == null) {
            redirectAttributes.addFlashAttribute("error", "Error populating emissions.");
            return "redirect:/emissions";
        }
        emissionRepository.saveAll(emissions);
        redirectAttributes.addFlashAttribute("success", "Emissions populated successfully.");
        return "redirect:/emissions";
    }

    public String deleteAllEmissions(RedirectAttributes redirectAttributes) {
        emissionRepository.deleteAll();
        redirectAttributes.addFlashAttribute("success", "All emissions deleted successfully.");
        return "redirect:/emissions";
    }

}
