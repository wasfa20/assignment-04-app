package com.example.assignment_04_android.model;

import java.io.Serializable;

public class NewsArticle implements Serializable {
    private int id; // For SQLite
    private String title;
    private String description;
    private String url;
    private String publishedAt;

    public NewsArticle(int id, String title, String description, String url, String publishedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.publishedAt = publishedAt;
    }

    public NewsArticle(String title, String description, String url, String publishedAt) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.publishedAt = publishedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }
}
