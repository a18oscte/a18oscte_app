package com.example.a18oscte_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SnusReaderDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SnusReaderContract.Entry.TABLE_NAME + " (" +
                    SnusReaderContract.Entry._ID + " INTEGER PRIMARY KEY," +
                    SnusReaderContract.Entry.COLUMN_NAME_NAME + " TEXT," +
                    SnusReaderContract.Entry.COLUMN_NAME_PRIS  + " TEXT," +
                    SnusReaderContract.Entry.COLUMN_NAME_FORETAG + " TEXT," +
                    SnusReaderContract.Entry.COLUMN_NAME_MANGD+ " TEXT," +
                    SnusReaderContract.Entry.COLUMN_NAME_KATEGORI+ " TEXT," +
                    SnusReaderContract.Entry.COLUMN_NAME_NIKOTINHALT+ " TEXT," +
                    SnusReaderContract.Entry.COLUMN_NAME_BILD + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SnusReaderContract.Entry.TABLE_NAME;


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public SnusReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}