package com.example.demo.Domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String about;
    String title;
    int cost;
    String imgSrc;

    int count;

    public String getImgSrc() {
        return imgSrc;
    }

    public Product setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
        return this;
    }

    public int getId() {
        return id;
    }

    public Product setId(int id) {
        this.id = id;
        return this;
    }

    public String getAbout() {
        return about;
    }

    public Product setAbout(String about) {
        this.about = about;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Product setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getCost() {
        return cost;
    }

    public Product setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Product setCount(int count) {
        this.count = count;
        return this;
    }
}
