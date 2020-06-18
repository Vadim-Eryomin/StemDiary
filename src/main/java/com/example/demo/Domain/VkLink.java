package com.example.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VkLink {
    @Id
    int id;
    String avatarUrl;

    public int getId() {
        return id;
    }

    public VkLink setId(int id) {
        this.id = id;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public VkLink setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }
}
