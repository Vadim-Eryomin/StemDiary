package com.example.demo.Domain.ModelDomain;

public class Post {
    int id;
    String date;
    String urlToImage;
    String text;
    String urlToPost;

    public int getId() {
        return id;
    }

    public Post setId(int id) {
        this.id = id;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Post setDate(String date) {
        this.date = date;
        return this;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public Post setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
        return this;
    }

    public String getText() {
        return text;
    }

    public Post setText(String text) {
        this.text = text;
        return this;
    }

    public String getUrlToPost() {
        return urlToPost;
    }

    public Post setUrlToPost(String urlToPost) {
        this.urlToPost = urlToPost;
        return this;
    }
}
