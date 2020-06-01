package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String name;
    int teacherId;
    String teacherName;
    String imgSrc;
    String nextDate;
    long date;

    public int getId() {
        return id;
    }

    public Course setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public Course setTeacherId(int teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public Course setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
        return this;
    }

    public String getNextDate() {
        return nextDate;
    }

    public Course setNextDate(String nextDate) {
        this.nextDate = nextDate;
        return this;
    }

    public long getDate() {
        return date;
    }

    public Course setDate(long date) {
        this.date = date;
        return this;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Course setTeacherName(String teacherName) {
        this.teacherName = teacherName;
        return this;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacherId=" + teacherId +
                ", teacherName='" + teacherName + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", nextDate='" + nextDate + '\'' +
                ", date=" + date +
                '}';
    }
}
