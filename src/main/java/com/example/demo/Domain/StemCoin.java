package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StemCoin {
    @Id
    int id;

    int stemcoins;

    public int getId() {
        return id;
    }

    public StemCoin setId(int id) {
        this.id = id;
        return this;
    }

    public int getStemcoins() {
        return stemcoins;
    }

    public StemCoin setStemcoins(int stemcoins) {
        this.stemcoins = stemcoins;
        return this;
    }

    @Override
    public String toString() {
        return "StemCoin{" +
                "id=" + id +
                ", stemcoins=" + stemcoins +
                '}';
    }
}
