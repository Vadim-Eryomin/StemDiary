package com.example.demo.Repositories;

import com.example.demo.Domain.Basket;
import com.example.demo.Domain.CodeWord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketRepository extends CrudRepository<Basket, Integer> {
    List<Basket> findById(int id);
    List<Basket> findByCustomerId(int id);
    List<Basket> findAll();
}
