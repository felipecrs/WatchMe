package com.readme.app.model;

public class Book {
    private Integer _id;
    private String title;
    private String author;
    private Integer totalPages;
    private Integer actualPage;

    public Book(Integer _id, String title, String author, Integer totalPages) {
        this._id = _id;
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
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
