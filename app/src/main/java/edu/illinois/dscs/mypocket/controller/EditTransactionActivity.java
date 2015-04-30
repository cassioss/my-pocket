package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.CategoryDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.dao.TransactionDAO;

/**
 * @author Cassio
 * @version 1.0
 */
public class EditTransactionActivity extends ActionBarActivity {

    AccountDAO accountDB;
    CategoryDAO categoryDB;
    TransactionDAO transDB;
    String transName;
    String accountName;
    EditText transDescription;
    EditText transValue;
    EditText transDate;
    Spinner transCategory;
    Spinner transAccount;
    Cursor categoryCursor;
    Cursor accountCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

    transDescription = (EditText) findViewById(R.id.edit_transaction_description);
    transValue = (EditText) findViewById(R.id.edit_transaction_value);
    transDate = (EditText) findViewById(R.id.edit_transaction_date);
    transCategory = (Spinner) findViewById(R.id.edit_transaction_category);
    transAccount = (Spinner) findViewById(R.id.edit_transaction_account);

    transDB = new TransactionDAO(this);

    Intent calledIntent = getIntent();
    transName = calledIntent.getStringExtra("transName");
    accountName = calledIntent.getStringExtra("accountName");

     fillTransactionInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_transaction, menu);
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

    public void updateTransaction(View view) {
    }

    public void deleteTransaction(View view) {
    }

    public void fillTransactionInfo(){
        int category = 0;
        int account = 0;
        transDB.open();
        Cursor c = transDB.selectTrans(transName, accountName);

        while (!c.isAfterLast()) {
            transDescription.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TRANS_DESCRIPTION)));
            transValue.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TRANS_VALUE)));
            transDate.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TRANS_CREATION_DATE)));
            category = c.getInt(c.getColumnIndex((DBHelper.KEY_CATEGORY_ID)));
            account = c.getInt(c.getColumnIndex(DBHelper.KEY_ACCOUNT_ID));
            c.moveToNext();
        }

            //loadCategorySpinner(category);
            //loadAccountSpinner(account);

    }

    private void loadAccountSpinner(int accountID) {
        ArrayList<String> account = new ArrayList<>();
        accountCursor = accountDB.readData();
        accountCursor.moveToFirst();
        int valDefault = 0;

        while (!accountCursor.isAfterLast()) {
            int accID = categoryCursor.getInt(categoryCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_ID));
            String name = accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME));

            if(accID == accountID){
                account.add(0,name);
            }
            else{
                account.add(valDefault,name);
                valDefault++;
            }
            categoryCursor.moveToNext();

            account.add(name);
            accountCursor.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                account);

        transAccount.setAdapter(adapter);
        accountCursor.close();
    }

    private void loadCategorySpinner(int categoryID) {
        ArrayList<String> category = new ArrayList<>();
        categoryCursor = categoryDB.readData();
        int valDefault = 1;
        categoryCursor.moveToFirst();

        while (!categoryCursor.isAfterLast()) {
            int catID = categoryCursor.getInt(categoryCursor.getColumnIndex(DBHelper.KEY_CATEGORY_ID));
            String name = categoryCursor.getString(categoryCursor.getColumnIndex(DBHelper.KEY_CATEGORY_NAME));

            if(catID == categoryID){
                category.add(0,name);
            }
            else{
                category.add(valDefault,name);
                valDefault++;
            }
            categoryCursor.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                category);

        transCategory.setAdapter(adapter);
        categoryDB.close();
    }
}
