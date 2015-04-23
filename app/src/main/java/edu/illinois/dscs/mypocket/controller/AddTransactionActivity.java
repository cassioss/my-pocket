package edu.illinois.dscs.mypocket.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.CategoryDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.dao.TransactionDAO;

/**
 * @author Cassio, Dennis
 * @version 1.2
 * @since 1.1
 */
public class AddTransactionActivity extends ActionBarActivity implements OnItemSelectedListener {

    private Spinner categorySpinner;
    private Spinner accountSpinner;
    private CategoryDAO dbCategory;
    private TransactionDAO dbTransaction;
    private AccountDAO dbAccount;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        accountSpinner = (Spinner) findViewById(R.id.account_spinner);

        dbCategory = new CategoryDAO(this);
        dbAccount = new AccountDAO(this);
        dbTransaction = new TransactionDAO(this);
        dbCategory.open();
        dbAccount.open();
        dbTransaction.open();

        loadSpinnerDataCategory();
        loadSpinnerDataAccount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_transaction, menu);
        return true;
    }

    /**
     * Default selection for Settings menu.
     *
     * @param item an item on the Settings menu (only "Settings" by default).
     * @return a MenuItem equivalent to the item selected.
     */
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

    /**
     * Inserts all data inserted by the user into the Transactions table as a new transaction.
     *
     * @param view the View object (button) that calls this method.
     */
    public void saveTransaction(View view) {
        insertTransData();
        Intent goBackToMain = new Intent(this, MainActivity.class);
        startActivity(goBackToMain);
    }

    /**
     * Gets the transaction type choice.
     *
     * @return 0 for EXPENSE, 1 for INCOME.
     */
    private int getTransactionChoice() {
        int thisType = 0;
        RadioGroup choiceGroup = (RadioGroup) findViewById(R.id.transaction_choice_radio_group);
        int radioButtonID = choiceGroup.getCheckedRadioButtonId();
        switch (radioButtonID) {
            case R.id.expense_radio:
                break;
            case R.id.income_radio:
                thisType = 1;
                break;
        }
        return thisType;
    }

    /**
     * Gets the transactions's description.
     *
     * @return a string with the user input for description.
     */
    private String getDescription() {
        EditText description = (EditText) findViewById(R.id.description_entry);
        return description.getText().toString();
    }

    /**
     * Gets the transaction value, as a text entry, and turns it into a double number.
     *
     * @return the double number equivalent to the user input for the transaction value.
     */
    private double getValue() {
        EditText transactionValue = (EditText) findViewById(R.id.value_entry);
        return Double.valueOf(transactionValue.getText().toString());
    }

    /**
     * Gets the transaction date as a string.
     *
     * @return a string containing the transaction date.
     */
    private String getDate() {
        EditText dateView = (EditText) findViewById(R.id.date_field);
        return dateView.getText().toString();
    }

    /**
     * Inserts all user inputs into the Transactions table.
     */
    private void insertTransData() {
        String desc = getDescription();
        int type = getTransactionChoice();
        String date = getDate();
        Double value = getValue();

        // opening database
        dbTransaction.open();
        // insert data into table
        dbTransaction.insertData(type, desc, value, date, 1, 1);

    }

    /**
     * Loads all categories inside the Category dropdown for AddTransaction.
     */
    private void loadSpinnerDataCategory() {
        ArrayList<String> category = new ArrayList<String>();
        Cursor c = dbCategory.readData();
        c.moveToFirst();

        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(DBHelper.KEY_CATEGORY_NAME));
            category.add(name);
            c.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                category);

        categorySpinner.setAdapter(adapter);
        dbCategory.close();
    }

    /**
     * Loads all accounts inside the Account dropdown for AddTransaction.
     */
    private void loadSpinnerDataAccount() {
        ArrayList<String> account = new ArrayList<String>();
        Cursor c = dbAccount.readData();
        c.moveToFirst();

        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME));
            account.add(name);
            c.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                account);

        accountSpinner.setAdapter(adapter);
        dbAccount.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}