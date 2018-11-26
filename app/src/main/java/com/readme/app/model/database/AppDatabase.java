package com.readme.app.model.database;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.util.Log;

import com.readme.app.model.database.dao.BookDao;
import com.readme.app.model.database.dao.MovieDao;
import com.readme.app.model.database.dao.SeriesDao;
import com.readme.app.model.database.dao.UserDao;
import com.readme.app.model.entity.Book;
import com.readme.app.model.entity.Email;
import com.readme.app.model.entity.Movie;
import com.readme.app.model.entity.Series;
import com.readme.app.model.entity.User;
import com.readme.app.model.entity.UserHasMovie;
import com.readme.app.model.util.Converter;


import java.util.ArrayList;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Email.class, Book.class, Movie.class, UserHasMovie.class, Series.class}, version = AppDatabase.VERSION, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class AppDatabase extends RoomDatabase {

    public static final int VERSION = 1;
    public static final String NAME = "watchme";

    private static volatile AppDatabase instance = null;

    public static AppDatabase getInstance(final Context context) {
        if(instance == null) {
            synchronized (AppDatabase.class) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME).allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }

    public abstract BookDao getBookDao();

    public abstract UserDao getUserDao();

    public abstract MovieDao getMovieDao();

    public abstract SeriesDao getSeriesDao();

    //Android Database Manager
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SupportSQLiteDatabase sqlDB = this.getOpenHelper().getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.query(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
