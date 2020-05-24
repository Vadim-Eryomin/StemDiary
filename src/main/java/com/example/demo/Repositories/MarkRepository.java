package com.example.demo.Repositories;

import com.example.demo.Domain.CodeWord;
import com.example.demo.Domain.Mark;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarkRepository extends CrudRepository<Mark, Integer> {
    List<Mark> findById(int id);
    List<Mark> findAll();
    List<Mark> findByCourseIdAndDate(int courseId, long date);
    List<Mark> findByCourseIdAndDateAndPupilId(int courseId, long date, int pupilId);
}
