package com.readme.app.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static com.readme.app.model.entity.User.TABLE;

@Entity(tableName = TABLE)
public class User {
    public static final String TABLE = "user";

    public static final String ID = "id";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private Integer id = null;

    @ForeignKey(entity = Email.class, parentColumns = Email.ADDRESS, childColumns = EMAIL_ADDRESS, onDelete = CASCADE, onUpdate = CASCADE)
    @NonNull
    @ColumnInfo(name = EMAIL_ADDRESS)
    private String emailAddress;

    @ColumnInfo(name = PASSWORD)
    @NonNull
    private String password;

    @ColumnInfo(name = NAME)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(@NonNull String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean passwordMatch(String password) {
        return this.password.equals(password);
    }

}
