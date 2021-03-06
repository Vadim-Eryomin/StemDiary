package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegisterRequest {
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

    public int getId() {
        return id;
    }

    public RegisterRequest setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RegisterRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public RegisterRequest setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public RegisterRequest setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public RegisterRequest setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public RegisterRequest setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getCourse() {
        return course;
    }

    public RegisterRequest setCourse(String course) {
        this.course = course;
        return this;
    }
}
