package com.readme.app.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance = null;

    private static final String DATABASE_NAME = "readme_db";
    private static final int DATABASE_VERSION = 1;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Users table
        String sqlUsers = "CREATE TABLE "+Users.TABLE+"("
                +Users.ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Users.NAME+" TEXT, "
                +Users.EMAIL+" TEXT NOT NULL, "
                +Users.PASSWORD+" TEXT NOT NULL)";

        //Books table
        String sqlBooks = "CREATE TABLE "+ Books.TABLE+"("
                +Books.ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Books.USER_ID+" INTEGER NOT NULL, "
                +Books.TITLE+" TEXT NOT NULL, "
                +Books.AUTHOR+" TEXT NOT NULL, "
                +Books.TOTAL_PAGES+" INTEGER NOT NULL, "
                +Books.ACTUAL_PAGE+" INTEGER, "
                +Books.IMAGE+" BLOB, "
                +" FOREIGN KEY("+Books.USER_ID+") REFERENCES "+Users.TABLE+"("+Users.ID +") ON DELETE CASCADE)";

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

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";

        public static final String[] COLUMNS = {ID, NAME, EMAIL, PASSWORD};
    }

    public static class Books{
        public static final String TABLE = "books";

        public static final String ID = "id";
        public static final String USER_ID = "user_id";
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String TOTAL_PAGES = "totalPages";
        public static final String ACTUAL_PAGE = "actualPage";
        public static final String IMAGE = "image";

        public static final String[] COLUMNS = {ID, USER_ID, TITLE, AUTHOR, TOTAL_PAGES, ACTUAL_PAGE, IMAGE};
    }

}
