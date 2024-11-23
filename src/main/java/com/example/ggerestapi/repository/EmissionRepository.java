package com.example.ggerestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ggerestapi.entity.Emission;

public interface EmissionRepository extends JpaRepository<Emission, Long> {

    List<Emission> findByCategory(String category);
}
