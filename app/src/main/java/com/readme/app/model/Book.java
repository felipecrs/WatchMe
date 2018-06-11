package com.readme.app.model;

public class Book {
    private Integer _id;
    private Integer userId;
    private String title;
    private String author;
    private Integer totalPages;
    private Integer actualPage;

    public Book(Integer _id, Integer userId, String title, String author, Integer totalPages, Integer actualPage) {
        this._id = _id;
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.actualPage = actualPage;
    }

    public Book(Integer userId, String title, String author, Integer totalPages, Integer actualPage) {
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.actualPage = actualPage;
    }

    public Book() {

    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
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
}
