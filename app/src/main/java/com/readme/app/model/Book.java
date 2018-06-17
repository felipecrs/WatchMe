package com.readme.app.model;

import android.graphics.Bitmap;

public class Book {
    private Integer id;
    private Integer userId;
    private String title;
    private String author;
    private Integer totalPages;
    private Integer actualPage;
    private Bitmap image;

    public Book(Integer id, Integer userId, String title, String author, Integer totalPages, Integer actualPage, Bitmap image) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.actualPage = actualPage;
        this.image = image;
    }

    public Book(Integer userId, String title, String author, Integer totalPages, Integer actualPage, Bitmap image) {
        this.id = -1;
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.actualPage = actualPage;
        this.image = image;
    }

    public Book(Integer userId) {
        this.id = -1;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getActualPage() {
        return actualPage;
    }

    public void setActualPage(Integer actualPage) {
        this.actualPage = actualPage;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
