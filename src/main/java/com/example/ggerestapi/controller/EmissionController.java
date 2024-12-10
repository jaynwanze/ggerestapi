package com.example.ggerestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ggerestapi.entity.Emission;
import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.parser.EmissionParser;
import com.example.ggerestapi.repository.EmissionRepository;
import com.example.ggerestapi.service.EmissionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/api")
@Controller
public class EmissionController {

    @Autowired
    private EmissionService emissionService;
    @Autowired
    private EmissionRepository emissionRepository;

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

    @PostMapping(value = "/json/emissions/populate", produces = "text/plain")
    @ResponseBody
    public String jsonPopulateEmissions(HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            return "User is not authenticated. Please log in.";
        }
        if (emissionRepository.findAll().size() >= 242) {
            return "Emissions already populated";
        }
        List<Emission> emissions = EmissionParser.parseEmissions();
        emissionRepository.saveAll(emissions);
        return "Emissions populated successfully";
    }

    @GetMapping(value = "/json/emissions/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> jsonGetEmission(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("User is not authenticated. Please log in.");
        }
        Emission emission = emissionRepository.findById(id).orElse(null);

        if (emission == null) {
            return ResponseEntity.status(404).body("No emission found.");
        }

        return ResponseEntity.ok(emission);
    }

    @GetMapping(value = "/json/emissions", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> jsonGetAllEmissions(HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("User is not authenticated. Please log in.");
        }

        return ResponseEntity.ok(emissionRepository.findAll());
    }

    @GetMapping(value = "/json/emissions/category/{category}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> jsonGetEmissionsByCategory(@PathVariable String category, HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("User is not authenticated. Please log in.");
        }
        if (category == null) {
            return ResponseEntity.badRequest().body("Category is null");
        }

        List<Emission> emissions = emissionRepository.findByCategory(category);
        if (emissions.isEmpty()) {
            return ResponseEntity.status(404).body("No emissions found for the given category.");
        }

        return ResponseEntity.ok(emissions);
    }

    @PostMapping(value = "/json/emissions", consumes = "application/json", produces = "text/plain")
    @ResponseBody
    public String jsonCreateEmission(@RequestBody Emission emission, HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            return "User is not authenticated. Please log in.";
        }
        if (emission == null) {
            return "Error creating emission";
        }
        emissionRepository.save(emission);
        return "Emission created successfully";
    }

    @PutMapping(value = "/json/emissions", consumes = "application/json", produces = "text/plain")
    @ResponseBody
    public String jsonUpdateEmission(@RequestBody Emission updatedEmission, HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            return "User is not authenticated. Please log in.";
        }
        if (updatedEmission == null) {
            return "Emission is data is null";
        }
        Long id = updatedEmission.getId();
        Emission emission = emissionRepository.findById(id).orElse(null);
        if (emission == null) {
            return "Emission not found";
        }
        emission.setCategory(updatedEmission.getCategory());
        emission.setCategoryDescription(
                updatedEmission.getCategoryDescription());
        emission.setScenario(updatedEmission.getScenario());
        emission.setValue(updatedEmission.getValue());
        emission.setYear(updatedEmission.getYear());
        emission.setGasUnits(updatedEmission.getGasUnits());
        emission.setNk(updatedEmission.getNk());
        emission.setPredictedValue(updatedEmission.getPredictedValue());
        emission.setCountry(updatedEmission.getCountry());
        return "Emission updated successfully";
    }

    @DeleteMapping(value = "/json/emissions/{id}", produces = "text/plain")
    @ResponseBody
    public String jsonDeleteEmission(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            return "User is not authenticated. Please log in.";
        }
        if (emissionRepository.findById(id).orElse(null) == null) {
            return "Emission not found";
        }
        emissionRepository.deleteById(id);
        return "Emission deleted";
    }

    @DeleteMapping(value = "/json/emissions", produces = "text/plain")
    @ResponseBody
    public String jsonDeleteEmissionAll(HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            return "User is not authenticated. Please log in.";
        }
        if (emissionRepository.findAll().size() == 0) {
            return "No emissions to delete";
        }
        emissionRepository.deleteAll();
        return "Emissions deleted";
    }
}
