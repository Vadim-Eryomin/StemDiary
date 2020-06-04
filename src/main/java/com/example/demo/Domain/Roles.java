package com.example.demo.Domain;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.Reference;

import javax.persistence.*;

@Entity
@Table(name = "accounts_roles")
public class Roles {
    @Id
    private int id;

    private boolean isAdmin;
    private boolean isTeacher;

    public int getId() {
        return id;
    }

    public Roles setId(int id) {
        this.id = id;
        return this;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Roles setAdmin(boolean admin) {
        isAdmin = admin;
        return this;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public Roles setTeacher(boolean teacher) {
        isTeacher = teacher;
        return this;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "id=" + id +
                ", isAdmin=" + isAdmin +
                ", isTeacher=" + isTeacher +
                '}';
    }
}
