package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.CategoryDAO;

/**
 * @author Cassio
 * @version 1.0
 */
public class AddCategoryActivity extends ActionBarActivity {

    EditText categoryName;
    CategoryDAO categoryDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

    categoryName = (EditText) findViewById(R.id.category_name_edit_view);
    categoryDB = new CategoryDAO(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveCategory(View view) {
        String desc = categoryName.getText().toString();

        // opening database
        categoryDB.open();
        // insert data into table
        categoryDB.insertData(desc);
        startActivity(new Intent(this, AddTransactionActivity.class));
    }
}
