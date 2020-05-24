package com.example.demo.Repositories;

import com.example.demo.Domain.Roles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RolesRepository extends CrudRepository<Roles, Integer> {
    List<Roles> findById(int id);
    List<Roles> findAll();
}
