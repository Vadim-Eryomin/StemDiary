package com.example.demo.Repositories;

import com.example.demo.Domain.CodeWord;
import com.example.demo.Domain.ModelDomain.ChatMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Integer> {
    List<ChatMessage> findById(int id);
    List<ChatMessage> findAll();
}
