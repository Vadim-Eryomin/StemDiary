package com.example.demo.Repositories;

import com.example.demo.Domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    List<Product> findById(int id);
    List<Product> findByCostLessThanEqual(int cost);
    List<Product> findByTitleContainingIgnoreCase(String title);
    List<Product> findByTitle(String title);
    List<Product> findByCostLessThanEqualAndTitleContainingIgnoreCase(int cost, String title);
    List<Product> findAll();

}
