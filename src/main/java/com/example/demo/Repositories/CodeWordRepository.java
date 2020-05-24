package com.example.demo.Repositories;

import com.example.demo.Domain.CodeWord;
import com.example.demo.Domain.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CodeWordRepository extends CrudRepository<CodeWord, Integer> {
    List<CodeWord> findById(int id);
    List<CodeWord> findAll();
}
