package com.readme.app.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "books";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Users table
        database.execSQL("create table users(_id integer primary key autoincrement, "
                        +"name text, "
                        +"email text not null, "
                        +"password text not null)");

        //Books table
        database.execSQL("create table books(_id integer primary key autoincrement, "
                        +"user_id integer not null, "
                        +"title text not null, "
                        +"author text not null, "
                        +"totalPages integer not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static class Users{
        public static final String TABLE = "users";

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";

        public static final String[] COLUMNS = { _ID, NAME, EMAIL, PASSWORD};
    }

    public static class Books{
        public static final String TABLE = "books";
        public static final String _ID = "_id";
        public static final String USER_ID = "_id";
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String TOTAL_PAGES = "totalPages";

        public static final String[] COLUMNS = { _ID, USER_ID, TITLE, AUTHOR, TOTAL_PAGES};
    }
}
