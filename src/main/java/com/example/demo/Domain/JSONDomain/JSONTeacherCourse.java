package com.example.demo.Domain.JSONDomain;

public class JSONTeacherCourse {
    Object[] pupilId;
    Object[] pupilName;
    int teacherId;
    String teacherName;
    String avatarUrl;
    String preHomework;
    String homework;
    String postHomework;
    String preDate;
    String date;
    String postDate;
    String courseName;

    public Object[] getPupilId() {
        return pupilId;
    }

    public JSONTeacherCourse setPupilId(Object[] pupilId) {
        this.pupilId = pupilId;
        return this;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public JSONTeacherCourse setTeacherId(int teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public JSONTeacherCourse setTeacherName(String teacherName) {
        this.teacherName = teacherName;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public JSONTeacherCourse setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getPreHomework() {
        return preHomework;
    }

    public JSONTeacherCourse setPreHomework(String preHomework) {
        this.preHomework = preHomework;
        return this;
    }

    public String getHomework() {
        return homework;
    }

    public JSONTeacherCourse setHomework(String homework) {
        this.homework = homework;
        return this;
    }

    public String getPostHomework() {
        return postHomework;
    }

    public JSONTeacherCourse setPostHomework(String postHomework) {
        this.postHomework = postHomework;
        return this;
    }

    public String getPreDate() {
        return preDate;
    }

    public JSONTeacherCourse setPreDate(String preDate) {
        this.preDate = preDate;
        return this;
    }

    public String getDate() {
        return date;
    }

    public JSONTeacherCourse setDate(String date) {
        this.date = date;
        return this;
    }

    public String getPostDate() {
        return postDate;
    }

    public JSONTeacherCourse setPostDate(String postDate) {
        this.postDate = postDate;
        return this;
    }

    public Object[] getPupilName() {
        return pupilName;
    }

    public JSONTeacherCourse setPupilName(Object[] pupilName) {
        this.pupilName = pupilName;
        return this;
    }

    public String getCourseName() {
        return courseName;
    }

    public JSONTeacherCourse setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }
}
