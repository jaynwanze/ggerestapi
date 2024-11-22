package com.example.ggerestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ggerestapi.entity.Emission;

public interface EmissionRepository extends JpaRepository<Emission, Long> {
}
