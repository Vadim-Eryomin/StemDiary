package com.example.demo.Repositories;

import com.example.demo.Domain.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatusRepository extends CrudRepository<Status, Integer> {
    List<Status> findById(int id);
    List<Status> findAll();
}
