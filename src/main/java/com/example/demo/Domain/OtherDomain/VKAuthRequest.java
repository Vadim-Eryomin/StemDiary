package com.example.demo.Domain.OtherDomain;

public class VKAuthRequest {
    String access_token;
    int user_id;
    int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public VKAuthRequest setAccess_token(String access_token) {
        this.access_token = access_token;
        return this;
    }

    public int getUser_id() {
        return user_id;
    }

    public VKAuthRequest setUser_id(int user_id) {
        this.user_id = user_id;
        return this;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public VKAuthRequest setExpires_in(int expires_in) {
        this.expires_in = expires_in;
        return this;
    }

    @Override
    public String toString() {
        return "VKAuthRequest{" +
                "access_token='" + access_token + '\'' +
                ", user_id=" + user_id +
                ", expires_in=" + expires_in +
                '}';
    }
}
