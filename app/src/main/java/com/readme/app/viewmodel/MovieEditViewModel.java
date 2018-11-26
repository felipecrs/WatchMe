package com.readme.app.viewmodel;

import android.app.Application;

import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.MovieDao;
import com.readme.app.model.database.dao.SeriesDao;
import com.readme.app.model.entity.Movie;
import com.readme.app.view.activity.SessionManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MovieEditViewModel extends AndroidViewModel {

    private SessionManager sessionManager;

    private MovieDao dao;
    private Movie newItem;
    private Movie oldItem;

    private boolean editing;

    private boolean initialized = false;

    public MovieEditViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).getMovieDao();
        sessionManager = SessionManager.getInstance(application);
    }

    public void initialize(final int id) {
        if(!initialized) {
            editing = id != -1;

            newItem = new Movie();

            if(editing) {
                oldItem = dao.getById(id);
                newItem.setId(oldItem.getId());
            }

            initialized = true;
        }
    }

    public void save() {
        dao.save(newItem, sessionManager.getUserId());
    }

    public void delete() {
        dao.delete(oldItem, sessionManager.getUserId());
    }

    public boolean isEditing() {
        return editing;
    }

    public Movie getNewItem() {
        return newItem;
    }

    public Movie getOldItem() {
        return oldItem;
    }
}
