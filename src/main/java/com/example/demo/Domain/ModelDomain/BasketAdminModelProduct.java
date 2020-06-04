package com.example.demo.Domain.ModelDomain;

public class BasketAdminModelProduct {
    int id;
    String status;
    String productName;
    String customerName;

    public String getStatus() {
        return status;
    }

    public BasketAdminModelProduct setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public BasketAdminModelProduct setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BasketAdminModelProduct setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public int getId() {
        return id;
    }

    public BasketAdminModelProduct setId(int id) {
        this.id = id;
        return this;
    }
}
