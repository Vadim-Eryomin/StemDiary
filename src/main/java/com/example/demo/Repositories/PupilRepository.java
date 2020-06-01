package com.example.demo.Repositories;

import com.example.demo.Domain.Pupil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PupilRepository extends CrudRepository<Pupil, Integer> {
    List<Pupil> findById(int id);
    List<Pupil> findByCourseId(int id);
    List<Pupil> findByPupilId(int id);
    List<Pupil> findByCourseIdAndPupilId(int course, int pupil);
    List<Pupil> findAll();
}
