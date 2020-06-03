package com.example.demo.Repositories;

import com.example.demo.Domain.ArchiveRegisterRequest;
import com.example.demo.Domain.RegisterRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArchiveRegisterRequestRepository extends CrudRepository<ArchiveRegisterRequest, Integer> {
    List<ArchiveRegisterRequest> findById(int id);
    List<ArchiveRegisterRequest> findAll();
}
