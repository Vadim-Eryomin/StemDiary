package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Status {
    @Id
    int id;
    String status;

    public int getId() {
        return id;
    }

    public Status setId(int id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Status setStatus(String status) {
        this.status = status;
        return this;
    }
}

