package com.example.demo.Repositories;

import com.example.demo.Domain.Pupil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface PupilRepository extends CrudRepository<Pupil, Integer> {
    ArrayList<Pupil> findById(int id);
    ArrayList<Pupil> findByCourseId(int id);
    ArrayList<Pupil> findByPupilId(int id);
    ArrayList<Pupil> findByCourseIdAndPupilId(int course, int pupil);
    ArrayList<Pupil> findAll();
}
