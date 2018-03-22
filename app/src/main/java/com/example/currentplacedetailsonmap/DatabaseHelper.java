package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by macbook on 21/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CrewFinder.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE  groups ( _id INTEGER PRIMARY KEY,username TEXT,lat REAL,lng REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS groups";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
