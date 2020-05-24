package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    int courseId;
    long date;
    String homework;

    public int getId() {
        return id;
    }

    public Homework setId(int id) {
        this.id = id;
        return this;
    }

    public int getCourseId() {
        return courseId;
    }

    public Homework setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public long getDate() {
        return date;
    }

    public Homework setDate(long date) {
        this.date = date;
        return this;
    }

    public String getHomework() {
        return homework;
    }

    public Homework setHomework(String homework) {
        this.homework = homework;
        return this;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", date=" + date +
                ", homework='" + homework + '\'' +
                '}';
    }
}
