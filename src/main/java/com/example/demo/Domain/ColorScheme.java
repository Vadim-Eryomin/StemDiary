package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ColorScheme {
    @Id
    public int id;

    public int navigationColor;
    public int bodyColor;

    public int getId() {
        return id;
    }

    public ColorScheme setId(int id) {
        this.id = id;
        return this;
    }

    public int getNavigationColor() {
        return navigationColor;
    }

    public ColorScheme setNavigationColor(int navigationColor) {
        this.navigationColor = navigationColor;
        return this;
    }

    public int getBodyColor() {
        return bodyColor;
    }

    public ColorScheme setBodyColor(int bodyColor) {
        this.bodyColor = bodyColor;
        return this;
    }

    @Override
    public String toString() {
        return "ColorScheme{" +
                "id=" + id +
                ", navigationColor=" + navigationColor +
                ", bodyColor=" + bodyColor +
                '}';
    }
}
