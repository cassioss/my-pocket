package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.dscs.mypocket.model.Category;

/**
 * @author Dennis
 * @version 1.0
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

    public CategoryDAO open() {
        dbHandler = new DBHelper(Context);
        database = dbHandler.getWritableDatabase();
        return this;

    }

    public void close() {
        dbHandler.close();
    }

    public void insertData(String name) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_CATEGORY_NAME, name);
        database.insert(DBHelper.TABLE_CATEGORY, null, cv);
    }

    /**
     * Gets all the rows inside the Category table, equivalent to: SELECT * from Category;
     *
     * @return a Cursor object containing the data brought from the query.
     */
    public Cursor readData() {
        Cursor c = database.query(DBHelper.TABLE_CATEGORY, allCategories, null,
                null, null, null, null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Turns a cursor item into a full category.
     *
     * @param cursor the cursor with a given category in the database.
     * @return a category equivalent to the one pointed by the cursor.
     */
    private Category cursorToCategory(Cursor cursor) {
        Category category = new Category(0, null);
        category.setCategoryID(cursor.getInt(0));
        category.setName(cursor.getString(1));
        return category;
    }

    /**
     * Creates a Category object from all the data obtained from user interactions,
     * inserting it into the database.
     *
     * @param categoryID   the category ID (updated automatically).
     * @param categoryName the category name.
     * @return a Category object whose information is already inside the database.
     */
    public Category createCategory(int categoryID, String categoryName) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_CATEGORY_ID, categoryID);
        values.put(DBHelper.KEY_CATEGORY_NAME, categoryName);
        long insertId = database.insert(DBHelper.TABLE_CATEGORY, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_CATEGORY, allCategories,
                DBHelper.KEY_CATEGORY_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Category newCategory = cursorToCategory(cursor);
        cursor.close();

        return newCategory;
    }

    /**
     * Deletes a given category from the database.
     *
     * @param category the category marked for deletion.
     */
    public void deleteCategory(Category category) {
        long id = category.getCategoryID();
        System.out.println("Category deleted with id: " + id);
        database.delete(DBHelper.TABLE_CATEGORY, DBHelper.KEY_CATEGORY_ID + " = " + id, null);
    }

    /**
     * Gets all the categories from the database.
     *
     * @return a list with all items from the Category table turned into Category objects.
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TABLE_CATEGORY,
                allCategories, null, null, null, null, null);

        //cursor.moveToFirst();
        //while (!cursor.isAfterLast()) {
        //  Category category = cursorToCategory(cursor);
        //  categories.add(category);
        //  cursor.moveToNext();
        //}
        // make sure to close the cursor
        //cursor.close();
        return categories;
    }
}
