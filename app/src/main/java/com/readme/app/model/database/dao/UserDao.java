package com.readme.app.model.database.dao;

import com.readme.app.model.entity.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class UserDao {
    @Insert(onConflict = REPLACE)
    public abstract long save(User user);

    @Delete
    public abstract int delete(User user);

    @Query("SELECT * FROM "+User.TABLE)
    public abstract LiveData<List<User>> getAll();

    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.ID+" = :id")
    public abstract User getById(Integer id);

    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.ID+" = :id")
    public abstract LiveData<User> getLiveById(Integer id);

    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.EMAIL +" = :email")
    public abstract User getByEmail(String email);

}
