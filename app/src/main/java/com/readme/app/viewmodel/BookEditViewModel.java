package com.readme.app.viewmodel;

import android.app.Application;

import com.readme.app.model.entity.Book;
import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.BookDao;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BookEditViewModel extends AndroidViewModel {

    private BookDao bookDao;

    private Book newBook;

    private Book originalBook;

    private boolean editing;

    private boolean initialized;

    public BookEditViewModel(@NonNull Application application) {
        super(application);
        bookDao = AppDatabase.getInstance(application).getBookDao();
        initialized = false;
    }

    public void init(final int id) {
        if(!initialized) {
            editing = id != -1;

            if(editing) {
                originalBook = bookDao.getById(id);
            } else {

            }
            newBook = new Book();
            initialized = true;
        }
    }

    public void save() {

        bookDao.save(newBook);
    }

    public void delete() {
        bookDao.delete(newBook);
    }

    public boolean isEditing() {
        return editing;
    }

    public Book getNewBook() {
        return newBook;
    }

    public Book getOriginalBook() {
        return originalBook;
    }
}
