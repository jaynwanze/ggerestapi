package com.example.ggerestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ggerestapi.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
}
