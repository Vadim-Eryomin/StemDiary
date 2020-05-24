package com.example.demo.Repositories;

import com.example.demo.Domain.RegisterRequest;
import com.example.demo.Domain.Roles;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegisterRequestRepository extends CrudRepository<RegisterRequest, Integer> {
    List<RegisterRequest> findById(int id);
    List<RegisterRequest> findAll();
}