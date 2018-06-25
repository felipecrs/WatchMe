package com.readme.app.data;

import android.content.Context;

import com.readme.app.model.Book;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class BookRepository {

    private static BookRepository instance;

    public static BookRepository getInstance(Context context) {
        if(instance == null) {
            AppDatabase appDatabase = AppDatabase.getInstance(context);
            BookDao bookDao = appDatabase.getBookDao();
            instance = new BookRepository(bookDao);
        }
        return instance;
    }

    private final BookDao bookDao;

    private BookRepository(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public LiveData<List<Book>> getBooks() {
        return bookDao.selectAll();
    }

    public LiveData<Book> getBook(final int id) {
        return bookDao.selectById(id);
    }

    public void save(Book book) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> bookDao.save(book));
    }

    public void delete(Book book) {
        bookDao.delete(book);
    }

}
