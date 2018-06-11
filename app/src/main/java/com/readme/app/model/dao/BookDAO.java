package com.readme.app.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readme.app.control.DatabaseHelper;
import com.readme.app.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public BookDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDatabase(){
        if(database == null) {
            database = databaseHelper.getWritableDatabase();
        }

        return database;
    }

    private Book create(Cursor cursor){
        Book model = new Book(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Books._ID)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Books.USER_ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Books.TITLE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Books.AUTHOR)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Books.TOTAL_PAGES)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Books.ACTUAL_PAGE)));

        return model;
    }

    public List<Book> list(){
        Cursor cursor = getDatabase().query(DatabaseHelper.Books.TABLE,
                DatabaseHelper.Books.COLUMNS,
                null, null, null, null, null);
        List<Book> books = new ArrayList<Book>();
        while(cursor.moveToNext()){
            Book model = create(cursor);
            books.add(model);
        }
        cursor.close();
        return books;
    }

    public List<Book> listByUser(Integer user_id) {
        Cursor cursor = getDatabase().query(DatabaseHelper.Books.TABLE,
                DatabaseHelper.Books.COLUMNS,
                "user_id = ?", new String[]{ user_id.toString() }, null, null, null);
        List<Book> books = new ArrayList<Book>();
        while(cursor.moveToNext()){
            Book model = create(cursor);
            books.add(model);
        }
        cursor.close();
        return books;
    }

    public long save(Book model){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Books.USER_ID, model.getUserId());
        values.put(DatabaseHelper.Books.TITLE, model.getTitle());
        values.put(DatabaseHelper.Books.AUTHOR, model.getAuthor());
        values.put(DatabaseHelper.Books.TOTAL_PAGES, model.getTotalPages());
        values.put(DatabaseHelper.Books.ACTUAL_PAGE, model.getActualPage());

        if(model.get_id() != null){
            return getDatabase().update(DatabaseHelper.Books.TABLE, values, "_id = ?", new String[]{ model.get_id().toString()});
        }

        return getDatabase().insert(DatabaseHelper.Books.TABLE, null, values);
    }

    public boolean delete(Integer id){
        return getDatabase().delete(DatabaseHelper.Books.TABLE, "_id = ?", new String[]{ id.toString() }) > 0;
    }

    public Book searchByID(Integer id){
        Cursor cursor = getDatabase().query(DatabaseHelper.Books.TABLE, DatabaseHelper.Books.COLUMNS, "_id = ?", new String[]{ id.toString() }, null, null, null );

        if(cursor.moveToFirst()){
            Book model = create(cursor);
            cursor.close();
            return model;
        }
        return null;
    }

    public void close(){
        databaseHelper.close();
        database = null;
    }

}
