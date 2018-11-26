package com.readme.app.viewmodel;

import android.app.Application;

import com.readme.app.model.database.dao.MovieDao;
import com.readme.app.model.entity.Movie;
import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.entity.Series;
import com.readme.app.view.activity.SessionManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MoviesViewModel extends AndroidViewModel {
    private MovieDao dao;
    private SessionManager sessionManager;
    private LiveData<List<Movie>> items;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).getMovieDao();
        sessionManager = SessionManager.getInstance(application);
        items = dao.getByUserId(sessionManager.getUserId());
    }

    public LiveData<List<Movie>> getItems() {
        return items;
    }

    public List<Movie> getSearchResults(String searchText) {
        return dao.getByTitle(searchText);
    }
}