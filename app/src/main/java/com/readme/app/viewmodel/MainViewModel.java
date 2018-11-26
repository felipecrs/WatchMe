package com.readme.app.viewmodel;

import android.app.Application;

import com.readme.app.model.entity.User;
import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.UserDao;
import com.readme.app.view.activity.SessionManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private UserDao userDao;
    private SessionManager sessionManager;
    private LiveData<User> currentUser;

    public MainViewModel(@NonNull Application application) {
        super(application);
        userDao = AppDatabase.getInstance(application).getUserDao();
        sessionManager = SessionManager.getInstance(application);
        currentUser = userDao.getLiveById(sessionManager.getUserId());
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
}
