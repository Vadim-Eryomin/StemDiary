package com.example.demo.Domain.ModelDomain;

public class BasketModelProduct {
    String status;
    String productName;

    int id;

    public String getStatus() {
        return status;
    }

    public BasketModelProduct setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public BasketModelProduct setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public int getId() {
        return id;
    }

    public BasketModelProduct setId(int id) {
        this.id = id;
        return this;
    }
}
