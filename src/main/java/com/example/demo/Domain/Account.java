package com.example.demo.Domain;

import javax.persistence.*;

@Entity
@Table(name = "account_login")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String login;
    private String password;
    private String imgSrc;
    private String email;

    public int getId() {
        return id;
    }

    public Account setId(int id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Account setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public Account setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Account setEmail(String email) {
        this.email = email;
        return this;
    }
}
