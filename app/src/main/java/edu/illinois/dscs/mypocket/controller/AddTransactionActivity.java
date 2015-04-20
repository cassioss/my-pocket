package edu.illinois.dscs.mypocket.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import edu.illinois.dscs.mypocket.model.Category;

/**
 * @author Cassio, Dennis
 * @version 1.1
 * @since 1.0
 */
public class AddTransactionActivity extends ActionBarActivity implements OnItemSelectedListener {

    Spinner categorySpinner;
    Spinner accountSpinner;
    Button addTransButton;
    CategoryDAO dbCategory;
    TransactionDAO dbTransaction;
    AccountDAO dbAccount;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        accountSpinner = (Spinner) findViewById(R.id.account_spinner);
        addTransButton = (Button) findViewById(R.id.addTrans_id);

        dbCategory = new CategoryDAO(this);
        dbAccount = new AccountDAO(this);
        dbTransaction = new TransactionDAO(this);
        dbCategory.open();
        dbAccount.open();
        dbTransaction.open();

        loadSpinnerDataCategory();
        loadSpinnerDataAccount();


        //String[] categories = {"No Category"};
        //ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        //categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //categorySpinner.setAdapter(categoryAdapter);

        //accountSpinner = (Spinner) findViewById(R.id.account_spinner);
        //String[] accounts = {"MyPocket"};
        //ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accounts);
        //accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //accountSpinner.setAdapter(accountAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_transaction, menu);
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

    public void saveTransaction(View view) {
        insertTransData();
        Intent goBackToMain = new Intent(this, MainActivity.class);
        //Transaction newTransaction = new Transaction(getTransactionChoice(), getDescription(), getValue(), getDate(), getCategory(), getAccount());
        //MainActivity.lastTransactions.add(newTransaction);
        startActivity(goBackToMain);
    }

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

    private String getDescription() {
        EditText description = (EditText) findViewById(R.id.description_entry);
        return description.getText().toString();
    }

    private double getValue() {
        EditText transactionValue = (EditText) findViewById(R.id.value_entry);
        return Double.valueOf(transactionValue.getText().toString());
    }

    private String getDate() {
        EditText dateView = (EditText) findViewById(R.id.date_field);
        String dateText = dateView.getText().toString();
        return dateText;
    }

    private void insertTransData(){
        String desc = getDescription();
        int type = getTransactionChoice();
        String date = getDate();
        Double value = getValue();

        // opening database
        dbTransaction.open();
        // insert data into table
        dbTransaction.insertData(type, desc, value, date, 1, 1);

        //

    }

    private String getAccount() {
        return accountSpinner.getSelectedItem().toString();
    }

    private Category getCategory() {
        return null;
    }

    private void loadSpinnerDataCategory() {
        Cursor c = dbCategory.readData();
        ArrayList<String> category = new ArrayList<String>();

        c.moveToFirst();

        while (!c.isAfterLast()) {

            String name = c.getString(c.getColumnIndex(DBHelper.KEY_CATEGORY_NAME));
            category.add(name);
            c.moveToNext();
        }

        ArrayAdapter<String> aa1 = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                category);

        categorySpinner.setAdapter(aa1);

        dbCategory.close();
    }


    private void loadSpinnerDataAccount() {
        Cursor c = dbAccount.readData();

        ArrayList<String> account = new ArrayList<String>();

        c.moveToFirst();

        while (!c.isAfterLast()) {

            String name = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME));
            account.add(name);
            c.moveToNext();
        }

        ArrayAdapter<String> aa1 = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                account);

        accountSpinner.setAdapter(aa1);

        dbAccount.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}