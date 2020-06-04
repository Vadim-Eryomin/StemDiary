package com.example.demo.Repositories;

import com.example.demo.Domain.Homework;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HomeworkRepository extends CrudRepository<Homework, Integer> {
    List<Homework> findById(int id);
    List<Homework> findAll();
    List<Homework> findByCourseIdAndDate(int courseId, long date);
    List<Homework> findByCourseId(int courseId);

    boolean existsByCourseIdAndDate(int courseId, long date);
}
