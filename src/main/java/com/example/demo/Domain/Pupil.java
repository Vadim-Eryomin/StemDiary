package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Pupil {
    @Id
    @GeneratedValue
    int id;

    int courseId;
    int pupilId;

    public int getId() {
        return id;
    }

    public Pupil setId(int id) {
        this.id = id;
        return this;
    }

    public int getCourseId() {
        return courseId;
    }

    public Pupil setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public int getPupilId() {
        return pupilId;
    }

    public Pupil setPupilId(int pupilId) {
        this.pupilId = pupilId;
        return this;
    }

    @Override
    public String toString() {
        return "Pupil{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", pupilId=" + pupilId +
                '}';
    }
}
