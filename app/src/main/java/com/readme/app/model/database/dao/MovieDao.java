package com.readme.app.model.database.dao;

import com.readme.app.model.entity.Movie;
import com.readme.app.model.entity.Series;
import com.readme.app.model.entity.UserHasMovie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import static androidx.room.OnConflictStrategy.REPLACE;
import static androidx.room.RoomWarnings.CURSOR_MISMATCH;

@Dao
public abstract class MovieDao {
    @Insert(onConflict = REPLACE)
    abstract long saveMovie(Movie movie);

    @Insert(onConflict = REPLACE)
    abstract void saveUserHasMovie(UserHasMovie userHasMovie);

    @Transaction
    public void save(final Movie movie, Integer userId) {
        Integer movieId = (int) saveMovie(movie);
        UserHasMovie userHasMovie = new UserHasMovie(userId, movieId);
        saveUserHasMovie(userHasMovie);
    }

    @Delete
    abstract void deleteMovie(Movie movie);

    @Delete
    abstract void deleteUserHasMovie(UserHasMovie userHasMovie);

    @Transaction
    public void delete(Movie movie, Integer userId) {
        UserHasMovie userHasMovie = new UserHasMovie(userId, movie.getId());
        deleteUserHasMovie(userHasMovie);
        if(countUsersAssociatedWithMovie(movie.getId()) <= 0) {
            // No more users has this movie, should remove the movie itself as well
            deleteMovie(movie);
        }
    }

    @Query("SELECT COUNT(*) FROM "+UserHasMovie.TABLE+" WHERE "+UserHasMovie.MOVIE_ID+" = :movieId")
    public abstract int countUsersAssociatedWithMovie(Integer movieId);

    @Query("SELECT * FROM "+Movie.TABLE+" WHERE "+Movie.ID+" = :id")
    public abstract Movie getById(Integer id);

    @SuppressWarnings(CURSOR_MISMATCH)
    @Query("SELECT * FROM "+Movie.TABLE+" JOIN "+UserHasMovie.TABLE+" ON "+UserHasMovie.MOVIE_ID+" = "+Movie.ID+" WHERE "+UserHasMovie.USER_ID+" = :userId ORDER BY "+Movie.TITLE)
    public abstract LiveData<List<Movie>> getByUserId(Integer userId);

    @Query("SELECT * FROM "+Movie.TABLE+" WHERE "+Movie.TITLE+" LIKE '%' || :title || '%' ORDER BY "+Movie.TITLE)
    public abstract List<Movie> getByTitle(String title);
}