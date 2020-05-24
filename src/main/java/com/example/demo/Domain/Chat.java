package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    int firstId;
    int secondId;

    public int getId() {
        return id;
    }

    public Chat setId(int id) {
        this.id = id;
        return this;
    }

    public int getFirstId() {
        return firstId;
    }

    public Chat setFirstId(int firstId) {
        this.firstId = firstId;
        return this;
    }

    public int getSecondId() {
        return secondId;
    }

    public Chat setSecondId(int secondId) {
        this.secondId = secondId;
        return this;
    }
}
