package com.example.demo.Domain.ModelDomain;

public class PupilWithNameAndLastMark {
    int id;
    int courseId;
    int pupilId;
    String name;
    String surname;
    int markA;
    int markB;
    int markC;

    public int getId() {
        return id;
    }

    public PupilWithNameAndLastMark setId(int id) {
        this.id = id;
        return this;
    }

    public int getCourseId() {
        return courseId;
    }

    public PupilWithNameAndLastMark setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public int getPupilId() {
        return pupilId;
    }

    public PupilWithNameAndLastMark setPupilId(int pupilId) {
        this.pupilId = pupilId;
        return this;
    }

    public String getName() {
        return name;
    }

    public PupilWithNameAndLastMark setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public PupilWithNameAndLastMark setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public int getMarkA() {
        return markA;
    }

    public PupilWithNameAndLastMark setMarkA(int markA) {
        this.markA = markA;
        return this;
    }

    public int getMarkB() {
        return markB;
    }

    public PupilWithNameAndLastMark setMarkB(int markB) {
        this.markB = markB;
        return this;
    }

    public int getMarkC() {
        return markC;
    }

    public PupilWithNameAndLastMark setMarkC(int markC) {
        this.markC = markC;
        return this;
    }
}
