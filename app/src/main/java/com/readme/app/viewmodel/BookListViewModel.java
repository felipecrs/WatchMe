package com.readme.app.viewmodel;

import android.app.Application;
import android.content.Context;

import com.readme.app.data.BookRepository;
import com.readme.app.model.Book;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BookListViewModel extends AndroidViewModel {

    private BookRepository bookRepository;
    private boolean initialized;

    public BookListViewModel(@NonNull Application application) {
        super(application);
        bookRepository = BookRepository.getInstance(application);
        initialized = false;
    }

    public void init() {
        if(!initialized) {
            initialized = true;
        }
    }

    public LiveData<List<Book>> getAllBooks() {
        return bookRepository.getBooks();
    }

}
