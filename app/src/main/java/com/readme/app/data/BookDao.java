package com.readme.app.data;

import com.readme.app.model.Book;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface BookDao {

    @Query("SELECT COUNT(*) FROM " + Book.TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long save(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM " + Book.TABLE_NAME + " ORDER BY " + Book.TITLE + " ASC")
    LiveData<List<Book>> selectAll();

    @Query("SELECT * FROM " + Book.TABLE_NAME + " WHERE " + Book.ID + " = :id")
    LiveData<Book> selectById(int id);

    @Query("SELECT * FROM " + Book.TABLE_NAME + " WHERE " + Book.TITLE + " LIKE :titleOrAuthor OR " + Book.AUTHOR + " LIKE :titleOrAuthor ORDER BY " + Book.TITLE + " ASC")
    LiveData<List<Book>> selectByTitleOrAuthor(String titleOrAuthor);
}
