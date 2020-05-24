package com.example.demo.Repositories;

import com.example.demo.Domain.Course;
import com.example.demo.Domain.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    List<Course> findById(int id);
    List<Course> findAll();
    List<Course> findByTeacherId(int id);
}
