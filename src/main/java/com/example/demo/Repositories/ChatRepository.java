package com.example.demo.Repositories;

import com.example.demo.Domain.Chat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRepository extends CrudRepository<Chat, Integer> {
    List<Chat> findById(int id);
    List<Chat> findAll();
}
