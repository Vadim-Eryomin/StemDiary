package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CodeWord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String answer;

    public int getId() {
        return id;
    }

    public CodeWord setId(int id) {
        this.id = id;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public CodeWord setAnswer(String answer) {
        this.answer = answer;
        return this;
    }
}
