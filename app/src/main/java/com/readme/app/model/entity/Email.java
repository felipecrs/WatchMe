package com.readme.app.model.entity;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static com.readme.app.model.entity.Email.ADDRESS;
import static com.readme.app.model.entity.Email.TABLE;
import static com.readme.app.model.entity.Email.USER_ID;

@Entity(tableName = TABLE,
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = User.ID,
                        childColumns = USER_ID,
                        onDelete = CASCADE)},
        indices = {
            @Index(value = USER_ID),
            @Index(value = ADDRESS, unique = true)})
public class Email {
    public static final String TABLE = "email";

    public static final String ADDRESS = "address";
    public static final String USER_ID = "user_id";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ADDRESS)
    private String address;

    @NonNull
    @ColumnInfo(name = USER_ID)
    private Integer userId;

    public Email(@NonNull String address, @NonNull Integer userId) {
        this.address = address;
        this.userId = userId;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(@NonNull Integer userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address) &&
                Objects.equals(userId, email.userId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(address, userId);
    }
}
