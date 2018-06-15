package com.readme.app.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readme.app.model.Book;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "readmedatabase";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Users table
        String sqlUsers = "CREATE TABLE "+Users.TABLE+"("
                +Users._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Users.NAME+" TEXT, "
                +Users.EMAIL+" TEXT NOT NULL, "
                +Users.PASSWORD+" TEXT NOT NULL)";

        //Books table
        String sqlBooks = "CREATE TABLE "+ Books.TABLE+"("
                +Books._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Books.TITLE+" TEXT NOT NULL, "
                +Books.AUTHOR+" TEXT NOT NULL, "
                +Books.TOTAL_PAGES+" INTEGER NOT NULL, "
                +Books.ACTUAL_PAGE+" INTEGER, "
                +Books.USER_ID+" INTEGER NOT NULL, "
                +" FOREIGN KEY("+Books.USER_ID+") REFERENCES "+Users.TABLE+"("+Users._ID+") ON DELETE CASCADE)";

        db.execSQL(sqlUsers);
        db.execSQL(sqlBooks);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sqlUsers = "DROP TABLE IF EXISTS "+Users.TABLE;
        String sqlBooks = "DROP TABLE IF EXISTS "+Books.TABLE;

        db.execSQL(sqlUsers);
        db.execSQL(sqlBooks);

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
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
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String TOTAL_PAGES = "totalPages";
        public static final String ACTUAL_PAGE = "actualPage";
        public static final String USER_ID = "user_id";

        public static final String[] COLUMNS = { _ID, TITLE, AUTHOR, TOTAL_PAGES, ACTUAL_PAGE, USER_ID};
    }

}
