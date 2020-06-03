package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ArchiveRegisterRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String name;
    String surname;
    String password;
    String login;
    String imgSrc;
    String email;
    String phone;
    String course;

    boolean isAllowed;

    public int getId() {
        return id;
    }

    public ArchiveRegisterRequest setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ArchiveRegisterRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public ArchiveRegisterRequest setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ArchiveRegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public ArchiveRegisterRequest setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public ArchiveRegisterRequest setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ArchiveRegisterRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public ArchiveRegisterRequest setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getCourse() {
        return course;
    }

    public ArchiveRegisterRequest setCourse(String course) {
        this.course = course;
        return this;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public ArchiveRegisterRequest setAllowed(boolean allowed) {
        isAllowed = allowed;
        return this;
    }
}
