package com.readme.app.viewmodel;

import android.app.Application;

import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.SeriesDao;
import com.readme.app.model.entity.Movie;
import com.readme.app.model.entity.Series;
import com.readme.app.view.activity.SessionManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SeriesEditViewModel extends AndroidViewModel {

    private SessionManager sessionManager;

    private SeriesDao dao;
    private Series newItem;
    private Series oldItem;

    private boolean editing;

    private boolean initialized = false;

    public SeriesEditViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).getSeriesDao();
        sessionManager = SessionManager.getInstance(application);
    }

    public void initialize(final int id) {
        if(!initialized) {
            editing = id != -1;

            newItem = new Series();
            newItem.setUserId(sessionManager.getUserId());

            if(editing) {
                oldItem = dao.getById(id);
                newItem.setId(oldItem.getId());
            }

            initialized = true;
        }
    }

    public void save() {
        dao.save(newItem);
    }

    public void delete() {
        dao.delete(oldItem);
    }

    public boolean isEditing() {
        return editing;
    }

    public Series getNewItem() {
        return newItem;
    }

    public Series getOldItem() {
        return oldItem;
    }
}
