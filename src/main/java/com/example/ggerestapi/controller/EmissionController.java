package com.example.ggerestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ggerestapi.entity.Emission;
import com.example.ggerestapi.repository.EmissionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequestMapping("/emissionsservice")
@RestController
public class EmissionController {

    @Autowired
    private EmissionRepository emissionRepository;

    @GetMapping(value = "/emissions/{id}", consumes = "application/json", produces = "application/json")
    public Emission getEmission(Long id) {
        return emissionRepository.findById(id).orElse(null);
    }

    @GetMapping(value = "/emissions" ,consumes = "application/json", produces = "application/json")
    public List<Emission> getAllEmissions() {
        return emissionRepository.findAll();
    }

    @GetMapping(value = "/emissions/category/{category}", consumes = "application/json", produces = "application/json")
    public List<Emission> getEmissionsByCategory(String category) {
        return emissionRepository.findByCategory(category);
    }

    @PostMapping(value = "/emissions" ,consumes = "application/json", produces = "application/json")
    public Emission createEmission(Emission emission) {
        return emissionRepository.save(emission);
    }

    @PutMapping(value = "/emissions/{id}", consumes = "application/json", produces = "application/json")
    public Emission updateEmission(Long id, Emission updatedEmission) {
        Emission emission = emissionRepository.findById(id).orElse(null);
        if (emission == null) {
            return null;
        }
        emission.setCategory(updatedEmission.getCategory());
        emission.setCategoryDescription(updatedEmission.getCategoryDescription());
        emission.setScenario(updatedEmission.getScenario());
        emission.setValue(updatedEmission.getValue());
        emission.setYear(updatedEmission.getYear());
        emission.setGasUnits(updatedEmission.getGasUnits());
        emission.setNk(updatedEmission.getNk());
        emission.setPredictedValue(updatedEmission.getPredictedValue());
        emission.setCountry(updatedEmission.getCountry());
        return emissionRepository.save(emission);
    }

    @DeleteMapping(value = "/emissions/{id}", consumes = "application/json", produces = "text/plain")
    public boolean deleteEmission(Long id) {
        emissionRepository.deleteById(id);
        Emission existingEmission = emissionRepository.findById(id).orElse(null);
        return existingEmission == null ? true : false;
    }

}
