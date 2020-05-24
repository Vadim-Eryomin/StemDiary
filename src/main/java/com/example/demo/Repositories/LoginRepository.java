package com.example.demo.Repositories;

import com.example.demo.Domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoginRepository extends CrudRepository<Account, Integer> {
    List<Account> findByLoginAndPassword(String login, String password);
    List<Account> findById(int id);
}
