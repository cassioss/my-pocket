package edu.illinois.dscs.mypocket.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Cassio, Dennis
 * @version 1.0
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database

    private static final String DATABASE = "mypocket";
    private static final int VERSION = 1;

    // Table Account

    private static final String TABLE_ACCOUNT = "Account";

    private static final String KEY_ACCOUNT_ID = "accountID";
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static final String KEY_INITIAL_VALUE = "initialValue";
    private static final String KEY_ACCOUNT_ACTIVE = "accountActive";

    // Table Category

    private static final String TABLE_CATEGORY = "Category";

    private static final String KEY_CATEGORY_ID = "categoryID";
    private static final String KEY_CATEGORY_NAME = "categoryName";

    // Table Transactions

    private static final String TABLE_TRANSACTION = "Transactions";

    private static final String KEY_TRANS_ID = "transactionID";
    private static final String KEY_TRANS_TYPE = "transType";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CREATION_DATE = "creationDate";

    public DatabaseHandler(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    /**
     * Creates a database with three tables on the first time.
     *
     * @param db the database application.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAccountTable());
        db.execSQL(createCategoryTable());
        db.execSQL(createTransactionTable());
    }

    /**
     * CREATE TABLE ACCOUNT query string.
     *
     * @return a string containing the query necessary to create the Account table.
     */
    private String createAccountTable() {
        return "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ACCOUNT_NAME + " TEXT, " +
                KEY_INITIAL_VALUE + " REAL, " +
                KEY_ACCOUNT_ACTIVE + " INTEGER" +
                ");";
    }

    /**
     * CREATE TABLE CATEGORY query string.
     *
     * @return a string containing the query necessary to create the Category table.
     */
    private String createCategoryTable() {
        return "CREATE TABLE " + TABLE_CATEGORY + " (" +
                KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_CATEGORY_NAME + " TEXT" +
                ");";
    }

    /**
     * CREATE TABLE TRANSACTIONS query string.
     *
     * @return a string containing the query necessary to create the Transactions table.
     */
    private String createTransactionTable() {
        return "CREATE TABLE " + TABLE_TRANSACTION + " (" +
                KEY_TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TRANS_TYPE + " TEXT, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_CREATION_DATE + " TEXT, " +
                KEY_CATEGORY_ID + " INTEGER, " +
                KEY_ACCOUNT_ID + " INTEGER, " +
                "FOREIGN KEY (" + KEY_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_CATEGORY_ID + "), " +
                "FOREIGN KEY (" + KEY_ACCOUNT_ID + ") REFERENCES " + TABLE_ACCOUNT + "(" + KEY_ACCOUNT_ID + ");";
    }

    /**
     * Tells the database what to do in case there is an upgrade.
     *
     * @param db         the database application.
     * @param oldVersion the old version number.
     * @param newVersion the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }
}
