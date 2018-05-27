package com.readme.app.model;

public class Book {
    private Integer _id;
    private Integer user_id;
    private String title;
    private String author;
    private Integer totalPages;
    private Integer actualPage;

    public Book(Integer _id, Integer user_id, String title, String author, Integer totalPages, Integer actualPage) {
        this._id = _id;
        this.user_id = user_id;
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.actualPage = actualPage;
    }

    public Book(Integer user_id, String title, String author, Integer totalPages, Integer actualPage) {
        this.user_id = user_id;
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

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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
