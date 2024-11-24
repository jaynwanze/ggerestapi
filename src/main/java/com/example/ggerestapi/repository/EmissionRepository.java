package com.example.ggerestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.ggerestapi.entity.Emission;

public interface EmissionRepository extends JpaRepository<Emission, Long> {
    @Query("SELECT DISTINCT e.category FROM Emission e")
    List<String> findAllCategories();
}
