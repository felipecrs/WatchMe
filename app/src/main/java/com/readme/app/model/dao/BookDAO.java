package com.readme.app.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readme.app.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    public BookDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getReadableDatabase() {
        if (readableDatabase == null) {
            readableDatabase = databaseHelper.getReadableDatabase();
        }
        return readableDatabase;
    }

    private SQLiteDatabase getWritableDatabase() {
        if (writableDatabase == null) {
            writableDatabase = databaseHelper.getWritableDatabase();
        }
        return writableDatabase;
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
        Cursor cursor = query(null, null);

        List<Book> books = new ArrayList<Book>();
        if (cursor != null) {
            do {
                Book model = create(cursor);
                books.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return books;
    }

    public List<Book> listByUser(Integer user_id) {
        String selection = DatabaseHelper.Books.USER_ID + " = ?";
        String[] selectionArgs = new String[] { user_id.toString() };

        Cursor cursor = query(selection, selectionArgs);

        List<Book> books = new ArrayList<Book>();

        if (cursor != null) {
            do {
                Book model = create(cursor);
                books.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }
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
            return getWritableDatabase().update(DatabaseHelper.Books.TABLE, values, "_id = ?", new String[]{ model.get_id().toString()});
        }
        return getWritableDatabase().insert(DatabaseHelper.Books.TABLE, null, values);
    }

    public boolean delete(Integer id){
        return getWritableDatabase().delete(DatabaseHelper.Books.TABLE, "_id = ?", new String[]{ id.toString() }) > 0;
    }

    public Book searchByID(Integer id){
        String selection = DatabaseHelper.Books._ID + " = ?";
        String[] selectionArgs = new String[] { id.toString() };

        Cursor cursor = query(selection, selectionArgs);

        if(cursor != null) {
            Book model = create(cursor);
            cursor.close();
            return model;
        }
        return null;
    }

    public List<Book> getBookMatches(Integer user_id, String query) {
        String selection = DatabaseHelper.Books.USER_ID + " = ? AND ("+DatabaseHelper.Books.TITLE+" LIKE ? OR "+DatabaseHelper.Books.AUTHOR+" LIKE ?)";
        String[] selectionArgs = new String[] {user_id.toString(), "%"+query+"%", "%"+query+"%"};

        Cursor cursor = query(selection, selectionArgs);

        List<Book> books = new ArrayList<Book>();

        if(cursor != null ) {
            do {
                Book model = create(cursor);
                books.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return books;
    }

    private Cursor query(String selection, String[] selectionArgs) {

        Cursor cursor = getReadableDatabase().query(DatabaseHelper.Books.TABLE, DatabaseHelper.Books.COLUMNS, selection, selectionArgs, null, null, null );

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public void close(){
        databaseHelper.close();
        readableDatabase = null;
        writableDatabase = null;
    }

}
