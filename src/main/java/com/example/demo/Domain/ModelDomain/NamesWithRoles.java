package com.example.demo.Domain.ModelDomain;

public class NamesWithRoles {
    int id;
    String name;
    String surname;
    boolean admin;
    boolean teacher;

    public int getId() {
        return id;
    }

    public NamesWithRoles setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public NamesWithRoles setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public NamesWithRoles setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public boolean isAdmin() {
        return admin;
    }

    public NamesWithRoles setAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public NamesWithRoles setTeacher(boolean teacher) {
        this.teacher = teacher;
        return this;
    }
}
