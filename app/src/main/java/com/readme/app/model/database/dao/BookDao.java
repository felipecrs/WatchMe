package com.readme.app.model.database.dao;

import com.readme.app.model.entity.Book;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface BookDao {
    @Insert(onConflict = REPLACE)
    long save(Book book);

    @Delete
    int delete(Book book);

    @Query("SELECT * FROM "+Book.TABLE +" WHERE "+Book.ID+" = :id")
    Book getById(Integer id);

    @Query("SELECT * FROM "+Book.TABLE +" WHERE "+Book.USER_ID+" = :userId ORDER BY "+Book.TITLE)
    LiveData<List<Book>> getByUserId(Integer userId);

    @Query("SELECT * FROM "+Book.TABLE +" ORDER BY "+Book.TITLE+" ASC")
    LiveData<List<Book>> getAll();

    @Query("SELECT * FROM "+Book.TABLE +" WHERE ("+Book.TITLE+" LIKE '%' || :titleOrAuthor || '%' OR "+Book.AUTHOR+" LIKE :titleOrAuthor) AND "+Book.USER_ID+" = :userId ORDER BY "+Book.TITLE)
    List<Book> getByTitleOrAuthorAndUserId(String titleOrAuthor, Integer userId);

    @Query("SELECT * FROM "+Book.TABLE +" WHERE "+Book.TITLE+" LIKE '%' || :titleOrAuthor || '%' OR "+Book.AUTHOR+" LIKE :titleOrAuthor ORDER BY "+Book.TITLE)
    LiveData<List<Book>> getByTitleOrAuthor(String titleOrAuthor);
}
