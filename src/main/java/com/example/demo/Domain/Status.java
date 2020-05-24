package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    int status;

    public int getId() {
        return id;
    }

    public Status setId(int id) {
        this.id = id;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Status setStatus(int status) {
        this.status = status;
        return this;
    }
}

