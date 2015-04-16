package edu.illinois.dscs.mypocket.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * @author Cassio
 * @version 1.0
 */
public class BasicDAO {

    protected SQLiteDatabase database;
    protected DatabaseHandler dbHandler;

    /**
     * Basic DAO constructor.
     *
     * @param context the database context in the phone.
     */
    public BasicDAO(Context context) {
        dbHandler = new DatabaseHandler(context);
    }

    /**
     * Opens the database handler.
     *
     * @throws SQLException if the database cannot be reached.
     */
    public void open() throws SQLException {
        database = dbHandler.getWritableDatabase();
    }

    /**
     * Closes the database handler.
     */
    public void close() {
        dbHandler.close();
    }

}
