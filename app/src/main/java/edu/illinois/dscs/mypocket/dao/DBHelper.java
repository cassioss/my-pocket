package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Database

    public static final String DB_NAME = "POCKET.DB";
    public static final int DB_VERSION = 1;

    // Table Account

    public static final String TABLE_ACCOUNT = "Account";

    public static final String KEY_ACCOUNT_ID = "accountID";
    public static final String KEY_ACCOUNT_NAME = "accountName";
    public static final String KEY_INITIAL_VALUE = "initialValue";
    public static final String KEY_CURRENT_BALANCE = "initialValue";
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

    private static final String CREATE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " (" +
            KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_CATEGORY_NAME + " TEXT" +
            ");";

    private static final String CREATE_ACCOUNT = "CREATE TABLE " + TABLE_CATEGORY + " (" +
            KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_CATEGORY_NAME + " TEXT" +
            ");";

    private static final String CREATE_TRANSACTION = "CREATE TABLE " + TABLE_CATEGORY + " (" +
            KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_CATEGORY_NAME + " TEXT" +
            ");";

    private static final String INS_CAT = "INSERT INTO " + TABLE_CATEGORY + " (" + KEY_CATEGORY_NAME + ") VALUES (\"No category\");";

    // Insertion statements for default items

    private ContentValues valuesOfNoCategory() {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_CATEGORY_NAME, "No category");
        return values;
    }

    private ContentValues valuesOfMyPocket() {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_ACCOUNT_NAME, "MyPocket");
        values.put(DBHelper.KEY_INITIAL_VALUE, 0.0);
        values.put(DBHelper.KEY_CURRENT_BALANCE, 0.0);
        values.put(DBHelper.KEY_ACCOUNT_ACTIVE, 1);
        return values;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_ACCOUNT);
        db.execSQL(CREATE_TRANSACTION);
        db.insert(DBHelper.TABLE_CATEGORY, null, valuesOfNoCategory());
        db.insert(DBHelper.TABLE_ACCOUNT, null, valuesOfMyPocket());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_ACCOUNT);
        onCreate(db);
    }
}