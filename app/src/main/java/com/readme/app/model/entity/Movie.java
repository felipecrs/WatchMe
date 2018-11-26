package com.readme.app.model.entity;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.readme.app.model.entity.Movie.TABLE;

@Entity(tableName = TABLE)
public class Movie {
    public static final String TABLE = "movie";
    public static final String ID = "id";
    public static final String TITLE = "title";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private Integer id = null;

    @ColumnInfo(name = TITLE)
    @NonNull
    private String title;

    public Movie() {
    }

    @Ignore
    public Movie(Integer id, @NonNull String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) &&
                Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title);
    }
}
