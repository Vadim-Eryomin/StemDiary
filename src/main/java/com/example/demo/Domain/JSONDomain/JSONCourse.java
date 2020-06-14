package com.example.demo.Domain.JSONDomain;

public class JSONCourse {
    int pupilId;
    int teacherId;
    String teacherName;
    String avatarUrl;
    String preHomework;
    String homework;
    String postHomework;
    String preDate;
    String date;
    String postDate;

    public int getPupilId() {
        return pupilId;
    }

    public JSONCourse setPupilId(int pupilId) {
        this.pupilId = pupilId;
        return this;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public JSONCourse setTeacherId(int teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public JSONCourse setTeacherName(String teacherName) {
        this.teacherName = teacherName;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public JSONCourse setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getPreHomework() {
        return preHomework;
    }

    public JSONCourse setPreHomework(String preHomework) {
        this.preHomework = preHomework;
        return this;
    }

    public String getHomework() {
        return homework;
    }

    public JSONCourse setHomework(String homework) {
        this.homework = homework;
        return this;
    }

    public String getPostHomework() {
        return postHomework;
    }

    public JSONCourse setPostHomework(String postHomework) {
        this.postHomework = postHomework;
        return this;
    }

    public String getPreDate() {
        return preDate;
    }

    public JSONCourse setPreDate(String preDate) {
        this.preDate = preDate;
        return this;
    }

    public String getDate() {
        return date;
    }

    public JSONCourse setDate(String date) {
        this.date = date;
        return this;
    }

    public String getPostDate() {
        return postDate;
    }

    public JSONCourse setPostDate(String postDate) {
        this.postDate = postDate;
        return this;
    }
}
