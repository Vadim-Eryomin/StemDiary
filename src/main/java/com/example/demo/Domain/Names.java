package com.example.demo.Domain;

import javax.persistence.*;

@Entity
public class Names {
    @Id
    private int id;

    private String name;
    private String surname;

    public int getId() {
        return id;
    }

    public Names setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Names setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public Names setSurname(String surname) {
        this.surname = surname;
        return this;
    }
}
