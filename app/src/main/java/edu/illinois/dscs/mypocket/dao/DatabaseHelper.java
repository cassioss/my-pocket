package edu.illinois.dscs.mypocket.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dennis Cardoso on 4/16/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "mypocket";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE, null, VERSION);
    }
       

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
