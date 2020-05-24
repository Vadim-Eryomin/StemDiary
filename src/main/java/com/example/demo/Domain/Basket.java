package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    int customerId;
    int productId;

    public int getId() {
        return id;
    }

    public Basket setId(int id) {
        this.id = id;
        return this;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Basket setCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public int getProductId() {
        return productId;
    }

    public Basket setProductId(int productId) {
        this.productId = productId;
        return this;
    }
}
