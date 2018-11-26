package com.readme.app.model.database.dao;

import com.readme.app.model.entity.Series;
import com.readme.app.model.entity.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SeriesDao {
    @Insert(onConflict = REPLACE)
    long save(Series book);

    @Delete
    int delete(Series book);

    @Query("SELECT * FROM "+Series.TABLE +" WHERE "+Series.ID+" = :id")
    Series getById(Integer id);

    @Query("SELECT * FROM "+Series.TABLE +" WHERE "+Series.USER_ID+" = :userId ORDER BY "+Series.TITLE+" ASC")
    LiveData<List<Series>> getByUserId(Integer userId);

    @Query("SELECT * FROM "+Series.TABLE +" WHERE "+Series.TITLE+" LIKE '%' || :titleOrAuthor || '%' AND "+Series.USER_ID+" = :userId ORDER BY "+Series.TITLE)
    List<Series> getByTitleAndUserId(String titleOrAuthor, Integer userId);
}
