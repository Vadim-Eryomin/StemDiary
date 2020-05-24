package com.example.demo.Domain.ModelDomain;

public class CourseModel {
    int id;

    String teacherName;
    int teacherId;

    String courseName;

    public int getId() {
        return id;
    }

    public CourseModel setId(int id) {
        this.id = id;
        return this;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public CourseModel setTeacherName(String teacherName) {
        this.teacherName = teacherName;
        return this;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public CourseModel setTeacherId(int teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    public String getCourseName() {
        return courseName;
    }

    public CourseModel setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }
}
