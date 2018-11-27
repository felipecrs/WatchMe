package com.readme.app.viewmodel;

import android.app.Application;

import com.readme.app.model.entity.Book;
import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.BookDao;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BookEditViewModel extends AndroidViewModel {

    private BookDao bookDao;

    private Book newItem;

    private Book oldItem;

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

            newItem = new Book();

            if(editing) {
                oldItem = bookDao.getById(id);
                newItem.setId(oldItem.getId());
            }

            initialized = true;
        }
    }

    public void save() {

        bookDao.save(newItem);
    }

    public void delete() {
        bookDao.delete(oldItem);
    }

    public boolean isEditing() {
        return editing;
    }

    public Book getNewItem() {
        return newItem;
    }

    public Book getOldItem() {
        return oldItem;
    }
}
