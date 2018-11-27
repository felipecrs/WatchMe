package com.readme.app.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;
import static com.readme.app.model.entity.UserHasMovie.MOVIE_ID;
import static com.readme.app.model.entity.UserHasMovie.TABLE;
import static com.readme.app.model.entity.UserHasMovie.USER_ID;

@Entity(tableName = TABLE,
        primaryKeys = {USER_ID,MOVIE_ID},
        foreignKeys = {
            @ForeignKey(entity = User.class,
                    parentColumns = User.ID,
                    childColumns = USER_ID,
                    onDelete = CASCADE),
            @ForeignKey(entity = Movie.class,
                    parentColumns = Movie.ID,
                    childColumns = MOVIE_ID,
                    onDelete = CASCADE)},
        indices = {
            @Index(value = USER_ID),
            @Index(value = MOVIE_ID)})
public class UserHasMovie {
    public static final String TABLE = "user_has_movie";
    public static final String USER_ID = "user_id";
    public static final String MOVIE_ID = "movie_id";

    @ColumnInfo(name = USER_ID)
    @NonNull
    private Integer userId;

    @ColumnInfo(name = MOVIE_ID)
    @NonNull
    private Integer movieId;

    public UserHasMovie(@NonNull Integer userId,@NonNull Integer movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    @NonNull
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(@NonNull Integer userId) {
        this.userId = userId;
    }

    @NonNull
    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull Integer movieId) {
        this.movieId = movieId;
    }
}
