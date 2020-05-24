package com.example.demo.Repositories;

import com.example.demo.Domain.Names;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NamesRepository extends CrudRepository<Names, Integer> {
    List<Names> findById(int id);
    List<Names> findByName(String name);

}
