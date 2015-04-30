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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    RadioButton expenseButton;
    RadioButton incomeButton;
    EditText transDescription;
    EditText transValue;
    EditText transDate;
    Spinner transCategory;
    Spinner transAccount;
    Cursor categoryCursor;
    Cursor accountCursor;
    int transType = 0;
    int transID = 0;
    //private String description;
    //private int transactionChoice;
    //private String date;
    //private Double value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

    expenseButton = (RadioButton) findViewById(R.id.expense_radio);
    incomeButton = (RadioButton) findViewById(R.id.income_radio);
    transDescription = (EditText) findViewById(R.id.edit_transaction_description);
    transValue = (EditText) findViewById(R.id.edit_transaction_value);
    transDate = (EditText) findViewById(R.id.edit_transaction_date);
    transCategory = (Spinner) findViewById(R.id.edit_transaction_category);
    transAccount = (Spinner) findViewById(R.id.edit_transaction_account);
    accountDB = new AccountDAO(this);
    categoryDB = new CategoryDAO(this);
    transDB = new TransactionDAO(this);
    accountDB.open();
    categoryDB.open();

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
        String desc = getDescription();
        int type = getTransactionChoice();
        String date = getDate();
        Double value = getValue();
        if (type == 0) value *= -1.00;
        String category = transCategory.getSelectedItem().toString();
        String account = transAccount.getSelectedItem().toString();

        int categoryID = 0;
        int accountID = 0;

        categoryDB.open();
        accountDB.open();
        transDB.open();

        Cursor categoryC = categoryDB.getCategoryId(category);
        Cursor accountC = accountDB.getAccountId(account);

        categoryID = categoryC.getInt(categoryC.getColumnIndex(DBHelper.KEY_CATEGORY_ID));
        accountID = accountC.getInt(accountC.getColumnIndex(DBHelper.KEY_ACCOUNT_ID));

        // opening database
        transDB.open();
        // insert data into table
        transDB.updateData(transID, type, desc, value, date, categoryID, accountID);


        startActivity(goBackToDetailActivity());


       updateAccountValue(accountName);
    }

    public void updateAccountValue(String account) {
        Cursor totalTransCursor = transDB.getTransValueData(account);
        double totalAccount = totalTransCursor.getDouble(totalTransCursor.getColumnIndex("totalBalance"));

        accountDB.open();
        //Cursor initialValueCursor = accountDB.readInitialValue(accountName);
        //double initialValue = initialValueCursor.getDouble(totalTransCursor.getColumnIndex("initialValue"));
        //totalAccount += initialValue;
        accountDB.updateAccountValue(totalAccount, account);
    }

    private Intent goBackToDetailActivity() {

        Intent intent = new Intent(this, AccountDetailsActivity.class);
        intent.putExtra("accountName", accountName);
        return intent;
    }

    public void deleteTransaction(View view) {
        Cursor c = transDB.selectTrans(transName, accountName);
        transID = c.getInt(c.getColumnIndex(DBHelper.KEY_TRANS_ID));
        transDB.deleteTransaction(transID);
        startActivity(goBackToDetailActivity());
    }

    public void fillTransactionInfo(){
        String category = "";
        String account = "";
        transDB.open();
        Cursor c = transDB.selectTrans(transName, accountName);

        while (!c.isAfterLast()) {
            transID = c.getInt(c.getColumnIndex(DBHelper.KEY_TRANS_ID));
            transDescription.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TRANS_DESCRIPTION)));
            transValue.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TRANS_VALUE)));
            transDate.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TRANS_CREATION_DATE)));
            transType = c.getInt(c.getColumnIndex(DBHelper.KEY_TRANS_TYPE));
            category = c.getString(c.getColumnIndex((DBHelper.KEY_CATEGORY_NAME)));
            account = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME));
            c.moveToNext();
        }

        if(transType == 0){
            expenseButton.setChecked(true);
            incomeButton.setChecked(false);
        }else
        {
            expenseButton.setChecked(false);
            incomeButton.setChecked(true);
        } 

            loadCategorySpinner(category);
            loadAccountSpinner(account);

    }

    private void loadAccountSpinner(String accountName) {
        ArrayList<String> account = new ArrayList<>();
        accountCursor = accountDB.readData();
        accountCursor.moveToFirst();

        while (!accountCursor.isAfterLast()) {
            String name = accountCursor.getString(accountCursor.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME));
            account.add(name);
            accountCursor.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                account);

        transAccount.setAdapter(adapter);

        ArrayAdapter adapterTest = (ArrayAdapter) transAccount.getAdapter();
        int spinnerPosition = adapterTest.getPosition(accountName);
        transAccount.setSelection(spinnerPosition);

        accountCursor.close();
    }

    private void loadCategorySpinner(String categoryName) {
        ArrayList<String> category = new ArrayList<>();
        categoryCursor = categoryDB.readData();
        categoryCursor.moveToFirst();

        while (!categoryCursor.isAfterLast()) {
            String name = categoryCursor.getString(categoryCursor.getColumnIndex(DBHelper.KEY_CATEGORY_NAME));
            category.add(name);
            categoryCursor.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                category);

        transCategory.setAdapter(adapter);

        ArrayAdapter adapterTest = (ArrayAdapter) transCategory.getAdapter();
        int spinnerPosition = adapterTest.getPosition(categoryName);
        transCategory.setSelection(spinnerPosition);

        categoryDB.close();
    }

    public String getDescription() {
        return transDescription.getText().toString();
    }

    public int getTransactionChoice() {
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

    public String getDate() {
        return transDate.getText().toString();
    }

    public Double getValue() {
        String cleanValue = transValue.getText().toString().replaceAll("[$,.]", "");
        double doubleValue100Times = Double.parseDouble(cleanValue);
        return doubleValue100Times / 100.0;
    }
}
