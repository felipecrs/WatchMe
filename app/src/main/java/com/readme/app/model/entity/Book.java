package com.readme.app.model.entity;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static com.readme.app.model.entity.Book.TABLE;

@Entity(tableName = TABLE)
public class Book {
    public static final String TABLE = "book";
    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String PAGES = "pages";
    public static final String CURRENT_PAGE = "current_page";
    public static final String COVER = "cover";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private Integer id = null;

    @ForeignKey(entity = User.class, parentColumns = User.ID, childColumns = USER_ID, onDelete = CASCADE)
    @NonNull
    @ColumnInfo(name = USER_ID)
    private Integer userId = null;

    @ColumnInfo(name = TITLE)
    @NonNull
    private String title;

    @ColumnInfo(name = AUTHOR)
    @NonNull
    private String author;

    @ColumnInfo(name = PAGES)
    @NonNull
    private Integer pages;

    @ColumnInfo(name = CURRENT_PAGE)
    private Integer currentPage;

    @ColumnInfo(name = COVER)
    private Bitmap cover;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(@NonNull Integer userId) {
        this.userId = userId;
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
    public Integer getPages() {
        return pages;
    }

    public void setPages(@NonNull Integer pages) {
        this.pages = pages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }
}
