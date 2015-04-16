package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.illinois.dscs.mypocket.model.Category;

/**
 * @author Dennis
 * @version 1.0
 */
public class CategoryDAO {

    private SQLiteDatabase database;
    private DatabaseHandler dbHandler;
    private String[] allCategories = {DatabaseHandler.TABLE_CATEGORY,
            DatabaseHandler.KEY_CATEGORY_ID,
            DatabaseHandler.KEY_CATEGORY_NAME};

    public CategoryDAO(Context context) {
        dbHandler = new DatabaseHandler(context);
    }

    public void open() throws SQLException {
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        dbHandler.close();
    }

    public Category createCategory(int categoryId, String categoryName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.KEY_CATEGORY_ID, categoryId);
        values.put(DatabaseHandler.KEY_CATEGORY_NAME, categoryName);
        long insertId = database.insert(DatabaseHandler.TABLE_CATEGORY, null, values);
        Cursor cursor = database.query(DatabaseHandler.TABLE_CATEGORY, allCategories,
                DatabaseHandler.KEY_CATEGORY_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Category newCategory = cursorToCategory(cursor);
        cursor.close();

        return newCategory;
    }

    public void deleteCategory(Category category) {
        long id = category.getCategoryID();
        System.out.println("Category deleted with id: " + id);
        database.delete(DatabaseHandler.TABLE_CATEGORY, DatabaseHandler.KEY_CATEGORY_ID + " = " + id, null);
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<Category>();

        Cursor cursor = database.query(DatabaseHandler.TABLE_CATEGORY,
                allCategories, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category category = cursorToCategory(cursor);
            categories.add(category);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return categories;
    }

    private Category cursorToCategory(Cursor cursor) {
        Category category = new Category(0, null);
        category.setCategoryID(cursor.getInt(0));
        category.setName(cursor.getString(1));
        return category;
    }

}
