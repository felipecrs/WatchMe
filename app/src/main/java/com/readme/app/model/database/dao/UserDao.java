package com.readme.app.model.database.dao;

import com.readme.app.model.entity.Email;
import com.readme.app.model.entity.User;
import com.readme.app.model.entity.UserWithEmails;

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
    abstract long save(User user);

    @Delete
    public abstract int delete(User user);

    @Query("DELETE FROM "+Email.TABLE+" WHERE "+Email.USER_ID+" = :userId")
    abstract void deleteAllEmailsByUserId(Integer userId);

    @Insert(onConflict = REPLACE)
    abstract void insertEmails(List<Email> emails);

    @Insert
    abstract void insertEmail(Email email);

    @Query("SELECT * FROM "+User.TABLE)
    public abstract LiveData<List<User>> getAll();

    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.ID+" = :id")
    public abstract User getById(Integer id);

    @Transaction
    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.ID+" = :id")
    public abstract UserWithEmails getUserWithEmailsById(Integer id);

    @Query("SELECT * FROM "+Email.TABLE+" WHERE "+Email.ADDRESS+" = :address")
    public abstract Email getEmailByAddress(String address);

    @Query("SELECT "+Email.ADDRESS+" FROM "+Email.TABLE+" WHERE "+Email.USER_ID+" = :userId")
    @NonNull
    public abstract List<String> getEmailAdressesByUserId(Integer userId);

    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.ID+" = :id")
    public abstract LiveData<User> getLiveById(Integer id);

    @Query("SELECT * FROM "+User.TABLE+" WHERE "+User.EMAIL_ADDRESS +" = :email")
    public abstract User getByEmail(String email);

    @Transaction
    void saveEmails(Integer userId, List<String> emailAdresses) {
        List<Email> emails = new ArrayList<>();
        emailAdresses.forEach(emailAdress -> emails.add(new Email(emailAdress, userId)));
        emails.sort((o1, o2) -> o1.getAddress().compareToIgnoreCase(o2.getAddress()));
        deleteAllEmailsByUserId(userId);
        insertEmails(emails);
    }

    @Transaction
    public void saveUserWithEmails(User user, List<String> emailAdresses) {
        Integer userId = (int) save(user);
        saveEmails(userId,emailAdresses);
    }

    @Transaction
    public void saveUserWithEmails(UserWithEmails userWithEmails) {
        Integer userId = (int) save(userWithEmails.getUser());
        for (String emailAdress : userWithEmails.getEmailAdresses()) {
            if(emailAdress.equals(userWithEmails.getUser().getEmailAddress())){
                userWithEmails.getEmailAdresses().remove(emailAdress);
                userWithEmails.getEmailAdresses().add(0, emailAdress);
                break;
            }
        }
        saveEmails(userId,userWithEmails.getEmailAdresses());
    }
}
