package com.readme.app.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.readme.app.model.Book.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Book {
    public static final String TABLE_NAME = "books";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String TOTAL_PAGES = "total_pages";
    public static final String ACTUAL_PAGE = "actual_page";
    public static final String COVER = "cover";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private Integer id;

    @ColumnInfo(name = TITLE)
    @NonNull
    private String title;

    @ColumnInfo(name = AUTHOR)
    @NonNull
    private String author;

    @ColumnInfo(name = TOTAL_PAGES)
    @NonNull
    private Integer totalPages;

    @ColumnInfo(name = ACTUAL_PAGE)
    private Integer actualPage;

    @ColumnInfo(name = COVER)
    private Bitmap cover;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@NonNull String author) {
        this.author = author;
    }

    @NonNull
    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(@NonNull Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getActualPage() {
        return actualPage;
    }

    public void setActualPage(Integer actualPage) {
        this.actualPage = actualPage;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }
}
