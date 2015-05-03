package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Data access object for the Category table.
 *
 * @author Cassio, Dennis
 * @version 1.2
 * @since 1.1
 */
public class CategoryDAO {

    protected SQLiteDatabase database;
    protected DBHelper dbHandler;
    protected Context Context;

    private String[] allCategories = {DBHelper.KEY_CATEGORY_ID,
            DBHelper.KEY_CATEGORY_NAME};

    /**
     * DAO constructor for categories.
     *
     * @param context the database context in the phone.
     */
    public CategoryDAO(Context context) {
        Context = context;
    }

    /**
     * Opens the database.
     *
     * @return itself (the object instance of CategoryDAO calling the method).
     */
    public CategoryDAO open() {
        dbHandler = new DBHelper(Context);
        database = dbHandler.getWritableDatabase();
        return this;
    }

    /**
     * Closes the database.
     */
    public void close() {
        dbHandler.close();
    }

    ////////////////////
    // SELECT queries //
    ////////////////////

    /**
     * Gets all the rows inside the Category table, equivalent to: SELECT * from Category;
     *
     * @return a Cursor object containing the data brought from the query.
     */
    public Cursor selectAll() {
        Cursor c = database.query(DBHelper.TABLE_CATEGORY, allCategories, null,
                null, null, null, null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Gets a category ID based on its name.
     *
     * @param categoryName the category name.
     * @return a Cursor object containing the category ID.
     */
    public Cursor selectCategoryIDFrom(String categoryName) {
        Cursor c = database.rawQuery("SELECT categoryID FROM category WHERE categoryName LIKE '" + categoryName + "';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    //////////////////
    // INSERT query //
    //////////////////

    /**
     * Inserts a new category inside the Category table, based on its name.
     *
     * @param categoryName the category name.
     */
    public void insertNewCategory(String categoryName) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_CATEGORY_NAME, categoryName);
        database.insert(DBHelper.TABLE_CATEGORY, null, cv);
    }
}