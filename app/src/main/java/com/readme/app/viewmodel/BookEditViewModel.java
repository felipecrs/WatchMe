package com.readme.app.viewmodel;

import android.app.Application;

import com.readme.app.data.BookRepository;
import com.readme.app.model.Book;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BookEditViewModel extends AndroidViewModel {

    private BookRepository bookRepository;

    private Book newBook;

    private LiveData<Book> originalBook;

    private boolean editing;

    private boolean initialized;

    public BookEditViewModel(@NonNull Application application) {
        super(application);
        bookRepository = BookRepository.getInstance(application);
        initialized = false;
    }

    public void init(final int id) {
        if(!initialized) {
            editing = id != -1;

            if(editing) {
                originalBook = bookRepository.getBook(id);
            } else {

            }
            newBook = new Book();
            initialized = true;
        }
    }

    public void save() {

        bookRepository.save(newBook);
    }

    public void delete() {
        bookRepository.delete(newBook);
    }

    public boolean isEditing() {
        return editing;
    }

    public Book getNewBook() {
        return newBook;
    }

    public LiveData<Book> getOriginalBook() {
        return originalBook;
    }
}
