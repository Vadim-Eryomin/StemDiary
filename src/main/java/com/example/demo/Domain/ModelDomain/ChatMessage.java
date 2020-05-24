package com.example.demo.Domain.ModelDomain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    int chatId;
    int whoId;
    String message;

    public int getId() {
        return id;
    }

    public ChatMessage setId(int id) {
        this.id = id;
        return this;
    }

    public int getChatId() {
        return chatId;
    }

    public ChatMessage setChatId(int chatId) {
        this.chatId = chatId;
        return this;
    }

    public int getWhoId() {
        return whoId;
    }

    public ChatMessage setWhoId(int whoId) {
        this.whoId = whoId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ChatMessage setMessage(String message) {
        this.message = message;
        return this;
    }
}
