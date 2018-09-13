package com.readme.app.data;

import android.content.Context;
import android.os.Build;

import com.readme.app.model.Book;
import com.readme.app.util.Converter;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Book.class}, version = AppDatabase.VERSION, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance = null;

    public static AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME).build();
        }
        return instance;
    }

    public static final int VERSION = 1;
    public static final String NAME = "readmedb";

    public abstract BookDao getBookDao();
}
