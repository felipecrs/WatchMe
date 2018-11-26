package com.readme.app.model.database.dao;

import com.readme.app.model.entity.Email;
import com.readme.app.model.entity.User;

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
    abstract long save(User user);

    @Delete
    public abstract int delete(User user);

    @Query("DELETE FROM "+Email.TABLE+" WHERE "+Email.USER_ID+" = :userId")
    abstract void deleteAllEmailsByUser(Integer userId);

    @Insert
    abstract void insertEmails(List<Email> emails);

    @Query("SELECT * FROM "+User.TABLE)
    public abstract LiveData<List<User>> getAll();

    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.ID+" = :id")
    public abstract User getById(Integer id);

    @Query("SELECT * FROM "+Email.TABLE+" WHERE "+Email.ADDRESS+" = :address")
    public abstract Email getEmail(String address);

    @Query("SELECT * FROM "+Email.TABLE+" WHERE "+Email.USER_ID+" = :userId")
    @NonNull
    public abstract List<Email> getEmailsByUserId(Integer userId);

    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.ID+" = :id")
    public abstract LiveData<User> getLiveById(Integer id);

    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.EMAIL_ADDRESS +" = :email")
    public abstract User getByEmail(String email);

    @Transaction
    void saveEmails(Integer userId, List<Email> emails) {
        deleteAllEmailsByUser(userId);
        insertEmails(emails);
    }

    @Transaction
    public void saveUserWithEmails(User user, List<Email> emails) {
        Integer userId = (int) save(user);
        emails.forEach(email -> email.setUserId(userId));
        saveEmails(user.getId(),emails);
    }
}
