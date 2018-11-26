package com.readme.app.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;
import static com.readme.app.model.entity.UserHasMovie.MOVIE_ID;
import static com.readme.app.model.entity.UserHasMovie.TABLE;
import static com.readme.app.model.entity.UserHasMovie.USER_ID;

@Entity(tableName = TABLE,primaryKeys = {USER_ID,MOVIE_ID})
public class UserHasMovie {
    public static final String TABLE = "user_has_movie";
    public static final String USER_ID = "user_id";
    public static final String MOVIE_ID = "movie_id";

    @ForeignKey(entity = User.class, parentColumns = User.ID, childColumns = USER_ID, onDelete = CASCADE)
    @ColumnInfo(name = USER_ID)
    @NonNull
    private Integer userId;

    @ForeignKey(entity = Movie.class, parentColumns = Movie.ID, childColumns = MOVIE_ID)
    @ColumnInfo(name = MOVIE_ID)
    @NonNull
    private Integer movieId;

    public UserHasMovie(Integer userId, Integer movieId) {
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
