package com.example.demo.Repositories;

import com.example.demo.Domain.Pupil;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PupilRepository extends CrudRepository<Pupil, Integer> {
    List<Pupil> findById(int id);
    List<Pupil> findByCourseId(int id);
    List<Pupil> findAll();
}
