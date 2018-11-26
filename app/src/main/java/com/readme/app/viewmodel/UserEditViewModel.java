package com.readme.app.viewmodel;

import android.app.Application;

import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.SeriesDao;
import com.readme.app.model.database.dao.UserDao;
import com.readme.app.model.entity.Series;
import com.readme.app.model.entity.User;
import com.readme.app.model.entity.UserWithEmails;
import com.readme.app.view.activity.SessionManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class UserEditViewModel extends AndroidViewModel {

    private SessionManager sessionManager;

    private UserDao dao;
    private UserWithEmails newItem;
    private UserWithEmails oldItem;

    private boolean editing;

    private boolean initialized = false;

    public UserEditViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).getUserDao();
        sessionManager = SessionManager.getInstance(application);
    }

    public void initialize(final int id) {
        if(!initialized) {
            editing = id != -1;

            newItem = new UserWithEmails();

            if(editing) {
                oldItem = dao.getUserWithEmailsById(id);
                newItem.getUser().setId(oldItem.getUser().getId());
            }

            initialized = true;
        }
    }

    public void save() {
        dao.saveUserWithEmails(newItem);
    }

    public void delete() {
        dao.delete(oldItem.getUser());
    }

    public boolean isEditing() {
        return editing;
    }

    public UserWithEmails getNewItem() {
        return newItem;
    }

    public UserWithEmails getOldItem() {
        return oldItem;
    }
}
