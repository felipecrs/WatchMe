package com.readme.app.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static com.readme.app.model.entity.Series.TABLE;

@Entity(tableName = TABLE)
public class Series {
    public static final String TABLE = "series";
    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String TITLE = "title";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private Integer id = null;

    @ForeignKey(entity = User.class, parentColumns = User.ID, childColumns = USER_ID, onDelete = CASCADE)
    @ColumnInfo(name = USER_ID)
    @NonNull
    private Integer userId = null;

    @ColumnInfo(name = TITLE)
    @NonNull
    private String title;

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
}
