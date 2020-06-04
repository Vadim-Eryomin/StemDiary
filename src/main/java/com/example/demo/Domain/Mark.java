package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    int pupilId;
    long date;
    int courseId;
    int markA;
    int markB;
    int markC;
    int total;

    public int getId() {
        return id;
    }

    public Mark setId(int id) {
        this.id = id;
        return this;
    }

    public long getDate() {
        return date;
    }

    public Mark setDate(long date) {
        this.date = date;
        return this;
    }

    public int getCourseId() {
        return courseId;
    }

    public Mark setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public int getMarkA() {
        return markA;
    }

    public Mark setMarkA(int markA) {
        this.markA = markA;
        return this;
    }

    public int getMarkB() {
        return markB;
    }

    public Mark setMarkB(int markB) {
        this.markB = markB;
        return this;
    }

    public int getMarkC() {
        return markC;
    }

    public Mark setMarkC(int markC) {
        this.markC = markC;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public Mark setTotal(int total) {
        this.total = total;
        return this;
    }

    public int getPupilId() {
        return pupilId;
    }

    public Mark setPupilId(int pupilId) {
        this.pupilId = pupilId;
        return this;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", pupilId=" + pupilId +
                ", date=" + date +
                ", courseId=" + courseId +
                ", markA=" + markA +
                ", markB=" + markB +
                ", markC=" + markC +
                ", total=" + total +
                '}';
    }
}
