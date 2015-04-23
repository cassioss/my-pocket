package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Database

    public static final String DB_NAME = "MYPOCKET.DB";
    public static final int DB_VERSION = 1;

    // Table Account

    public static final String TABLE_ACCOUNT = "Account";

    public static final String _ID = "accountID";
    public static final String KEY_ACCOUNT_ID = "accountID";
    public static final String KEY_ACCOUNT_NAME = "accountName";
    public static final String KEY_ACCOUNT_INITIAL_VALUE = "initialValue";
    public static final String KEY_ACCOUNT_CURRENT_BALANCE = "currentBalance";
    public static final String KEY_ACCOUNT_ACTIVE = "accountActive";

    // Table Category

    public static final String TABLE_CATEGORY = "Category";

    public static final String KEY_CATEGORY_ID = "categoryID";
    public static final String KEY_CATEGORY_NAME = "categoryName";

    // Table Transactions

    public static final String TABLE_TRANSACTION = "Transactions";

    public static final String KEY_TRANS_ID = "transactionID";
    public static final String KEY_TRANS_TYPE = "transType";
    public static final String KEY_TRANS_DESCRIPTION = "description";
    public static final String KEY_TRANS_VALUE = "transactionValue";
    public static final String KEY_TRANS_CREATION_DATE = "creationDate";

    // Table creation statements

    private static final String CREATE_CATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + " (" +
            KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_CATEGORY_NAME + " TEXT" +
            ");";

    private static final String CREATE_ACCOUNT = "CREATE TABLE " + TABLE_ACCOUNT + " (" +
            KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_ACCOUNT_NAME + " TEXT, " +
            KEY_ACCOUNT_INITIAL_VALUE + " REAL, " +
            KEY_ACCOUNT_CURRENT_BALANCE + " REAL," +
            KEY_ACCOUNT_ACTIVE + " INTEGER" +
            ");";

    private static final String CREATE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION + " (" +
            KEY_TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_TRANS_TYPE + " TEXT, " +
            KEY_TRANS_DESCRIPTION + " TEXT, " +
            KEY_TRANS_VALUE + " REAL, " +
            KEY_TRANS_CREATION_DATE + " TEXT, " +
            KEY_CATEGORY_ID + " INTEGER, " +
            KEY_ACCOUNT_ID + " INTEGER, " +
            "FOREIGN KEY (" + KEY_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_CATEGORY_ID + "), " +
            "FOREIGN KEY (" + KEY_ACCOUNT_ID + ") REFERENCES " + TABLE_ACCOUNT + "(" + KEY_ACCOUNT_ID + ")" +
            ");";

    /**
     * DBHelper constructor.
     *
     * @param context the current application section.
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Insertion statements for default items

    /**
     * Since "No Category" is a default category, this is a method that sets all values for it when the database is created.
     *
     * @return a ContentValues object for "No Category" category.
     */
    private ContentValues valuesOfNoCategory() {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_CATEGORY_NAME, "No category");
        return values;
    }

    /**
     * Since "MyPocket" is a default account, this is a method that sets all values for it when the database is created.
     *
     * @return a ContentValues object for MyPocket account.
     */
    private ContentValues valuesOfMyPocket() {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_ACCOUNT_NAME, "MyPocket");
        values.put(DBHelper.KEY_ACCOUNT_INITIAL_VALUE, 0.0);
        values.put(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE, 0.0);
        values.put(DBHelper.KEY_ACCOUNT_ACTIVE, 1);
        return values;
    }

    // Methods for database creation and upgrade.

    /**
     * Creates all tables (if they do not already exist) and sets MyPocket and "No Category" items.
     *
     * @param db the app database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_ACCOUNT);
        db.execSQL(CREATE_TRANSACTION);
        db.insert(DBHelper.TABLE_CATEGORY, null, valuesOfNoCategory());
        db.insert(DBHelper.TABLE_ACCOUNT, null, valuesOfMyPocket());
    }

    /**
     * Recreates all tables and default items, after dropping them for upgrade purposes.
     *
     * @param db         the app database.
     * @param oldVersion the old version number.
     * @param newVersion the new version number (has to be greater than oldVersion).
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }
}