package com.readme.app.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.readme.app.model.entity.User.TABLE;

@Entity(tableName = TABLE)
public class User {
    public static final String TABLE = "user";

    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private Integer id = null;

    @NonNull
    @ColumnInfo(name = EMAIL)
    private String email;

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
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
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
