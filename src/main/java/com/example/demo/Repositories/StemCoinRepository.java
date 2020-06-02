package com.example.demo.Repositories;

import com.example.demo.Domain.StemCoin;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StemCoinRepository extends CrudRepository<StemCoin, Integer> {
    List<StemCoin> findById(int id);
    List<StemCoin> findAll();
}
