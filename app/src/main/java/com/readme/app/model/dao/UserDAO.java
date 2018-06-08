package com.readme.app.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readme.app.control.DatabaseHelper;
import com.readme.app.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public UserDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDatabase(){
        if(database == null) {
            database = databaseHelper.getWritableDatabase();
        }
        return database;
    }

    private User create(Cursor cursor){
        User model = new User(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Users._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Users.NAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Users.EMAIL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Users.PASSWORD)));

        return model;
    }

    public List<User> list(){
        Cursor cursor = getDatabase().query(DatabaseHelper.Users.TABLE,
                                            DatabaseHelper.Users.COLUMNS,
                                            null, null, null, null, null);
        List<User> users = new ArrayList<User>();
        while(cursor.moveToNext()){
            User model = create(cursor);
            users.add(model);
        }
        cursor.close();

        return users;
    }

    public long save(User model){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Users.NAME, model.getName());
        values.put(DatabaseHelper.Users.EMAIL, model.getEmail());
        values.put(DatabaseHelper.Users.PASSWORD, model.getPassword());

        if(model.get_id() != null){
            return getDatabase().update(DatabaseHelper.Users.TABLE, values, "_id = ?", new String[]{ model.get_id().toString()});
        }
        return getDatabase().insert(DatabaseHelper.Users.TABLE, null, values);
    }

    public boolean delete(Integer id){
        // Delete all books from this user
        //Cursor cursor = getDatabase().query(DatabaseHelper.Books.TABLE,
        //        DatabaseHelper.Books.COLUMNS,
        //        "user_id = ?", new String[]{ id.toString() }, null, null, null);
        //while(cursor.moveToNext()) {
            getDatabase().delete(DatabaseHelper.Books.TABLE, "user_id = ?", new String[]{id.toString()});
        //}


        return getDatabase().delete(DatabaseHelper.Users.TABLE, "_id = ?", new String[]{Integer.toString(id)}) > 0;
    }

    public User searchByID(int id){
        Cursor cursor = getDatabase().query(DatabaseHelper.Users.TABLE, DatabaseHelper.Users.COLUMNS, "_id = ?", new String[]{Integer.toString(id)}, null, null, null );

        if(cursor.moveToFirst()){
            User model = create(cursor);
            cursor.close();
            return model;
        }
        return null;
    }

    public User searchByEmail(String email){
        Cursor cursor = getDatabase().query(DatabaseHelper.Users.TABLE, DatabaseHelper.Users.COLUMNS, "email = ?", new String[]{email}, null, null, null );

        if(cursor.moveToFirst()) {
            User model = create(cursor);
            cursor.close();
            return model;
        }
        return null;
    }

    public void close(){
        databaseHelper.close();
        database = null;
    }

    public boolean isValidCredentials(String email, String password) {
        User model = searchByEmail(email);
        return model != null && model.getPassword().equals(password);
    }
}
