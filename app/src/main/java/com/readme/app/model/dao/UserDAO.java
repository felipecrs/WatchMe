package com.readme.app.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.readme.app.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DatabaseHelper databaseHelper;

    public UserDAO(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    private User create(Cursor cursor){
        User model = new User(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Users.ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Users.NAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Users.EMAIL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Users.PASSWORD)));

        return model;
    }

    public List<User> list(){
        Cursor cursor = query(null, null);

        List<User> users = new ArrayList<>();

        if (cursor != null) {
            do {
                User model = create(cursor);
                users.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return users;
    }

    public long save(User model){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Users.NAME, model.getName());
        values.put(DatabaseHelper.Users.EMAIL, model.getEmail());
        values.put(DatabaseHelper.Users.PASSWORD, model.getPassword());

        if(model.getId() != -1){
            return databaseHelper.getWritableDatabase().update(DatabaseHelper.Users.TABLE, values, DatabaseHelper.Users.ID+" = ?", new String[]{ model.getId().toString()});
        }
        return databaseHelper.getWritableDatabase().insert(DatabaseHelper.Users.TABLE, null, values);
    }

    public boolean delete(Integer id){
        return databaseHelper.getWritableDatabase().delete(DatabaseHelper.Users.TABLE, DatabaseHelper.Users.ID+" = ?", new String[]{Integer.toString(id)}) > 0;
    }

    public User findById(Integer id){
        String selection = DatabaseHelper.Users.ID + " = ?";
        String[] selectionArgs = new String[] { id.toString() };

        Cursor cursor = query(selection, selectionArgs);

        if(cursor != null) {
            User model = create(cursor);
            cursor.close();
            return model;
        }
        return null;
    }

    public User findByEmail(String email){
        String selection = DatabaseHelper.Users.EMAIL + " = ?";
        String[] selectionArgs = new String[] { email };

        Cursor cursor = query(selection, selectionArgs);

        if(cursor != null) {
            User model = create(cursor);
            cursor.close();
            return model;
        }
        return null;
    }

    private Cursor query(String selection, String[] selectionArgs) {

        Cursor cursor = databaseHelper.getReadableDatabase().query(DatabaseHelper.Users.TABLE, DatabaseHelper.Users.COLUMNS, selection, selectionArgs, null, null, null );

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
    }
}
