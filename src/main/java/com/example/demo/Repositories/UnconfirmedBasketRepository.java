package com.example.demo.Repositories;

import com.example.demo.Domain.Basket;
import com.example.demo.Domain.CodeWord;
import com.example.demo.Domain.UnconfirmedBasket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnconfirmedBasketRepository extends CrudRepository<UnconfirmedBasket, Integer> {
    List<UnconfirmedBasket> findById(int id);
    List<UnconfirmedBasket> findByCustomerId(int id);
    List<UnconfirmedBasket> findAll();

    boolean existsByCustomerId(int id);
}
