package com.readme.app.viewmodel;

import android.app.Application;

import com.readme.app.model.entity.Book;
import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.BookDao;
import com.readme.app.view.activity.SessionManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BooksViewModel extends AndroidViewModel {
    private BookDao dao;
    private SessionManager sessionManager;
    private boolean initialized = false;
    private LiveData<List<Book>> items;

    public BooksViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).getBookDao();
        sessionManager = SessionManager.getInstance(application);
        items = dao.getByUserId(sessionManager.getUserId());
    }

    public void initialize() {
        if(!initialized) {
            // initialize
            initialized = true;
        }
    }

    public LiveData<List<Book>> getItems() {
        return items;
    }

    public List<Book> search(String newText) {
        return dao.getByTitleOrAuthorAndUserId(newText, sessionManager.getUserId());
    }
}
