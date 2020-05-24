package com.example.demo.Repositories;

import com.example.demo.Domain.ColorScheme;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ColorRepository extends CrudRepository<ColorScheme, Integer> {
    List<ColorScheme> findById(int id);
}
