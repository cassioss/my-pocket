package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.CategoryDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.dao.TransactionDAO;
import edu.illinois.dscs.mypocket.utils.CurrencyTextWatcher;
import edu.illinois.dscs.mypocket.utils.DateTextWatcher;
import edu.illinois.dscs.mypocket.utils.ValidationUtils;

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
    private EditText date;
    private EditText value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        accountSpinner = (Spinner) findViewById(R.id.account_spinner);

        dbCategory = new CategoryDAO(this);
        dbCategory.open();

        dbAccount = new AccountDAO(this);
        dbAccount.open();

        dbTransaction = new TransactionDAO(this);
        dbTransaction.open();

        loadSpinnerDataCategory();
        loadSpinnerDataAccount();

        date = (EditText) findViewById(R.id.date_field);
        date.addTextChangedListener(new DateTextWatcher(date));

        value = (EditText) findViewById(R.id.value_entry);
        value.addTextChangedListener(new CurrencyTextWatcher(value));
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
        if (ValidationUtils.invalidDescription(getApplicationContext(), getDescription())) return;
        if (ValidationUtils.invalidValue(getApplicationContext(), getValue())) return;
        if (ValidationUtils.invalidDate(getApplicationContext(), getDate())) return;
        insertTransData();
        startActivity(goBackToMainActivity());
    }

    /**
     * Gets the transaction type choice.
     *
     * @return 0 for EXPENSE, 1 for INCOME.
     */
    private int getTransactionChoice() {
        RadioGroup choiceGroup = (RadioGroup) findViewById(R.id.transaction_choice_radio_group);
        int radioButtonID = choiceGroup.getCheckedRadioButtonId();
        return radioButtonID == R.id.income_radio ? 1 : 0;
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
     * Trims the current description. If nothing is left, this method returns "No description".
     *
     * @return either the original description typed by the user, or "No description" if empty.
     */
    private String getDescriptionOrNothing() {
        String desc = getDescription();
        return desc.trim().length() > 0 ? desc : "No description";
    }

    /**
     * Gets the transaction value, as a text entry, and turns it into a double number.
     *
     * @return the double number equivalent to the user input for the transaction value.
     */
    private double getValue() {
        String cleanValue = value.getText().toString().replaceAll("[$,.]", "");
        double doubleValue100Times = Double.parseDouble(cleanValue);
        return doubleValue100Times / 100.0;
    }

    /**
     * Gets the transaction date as a string.
     *
     * @return a string containing the transaction date.
     */
    private String getDate() {
        return date.getText().toString();
    }

    /**
     * Inserts all user inputs into the Transactions table.
     */
    private void insertTransData() {
        String desc = getDescriptionOrNothing();
        int type = getTransactionChoice();
        String date = getDate();
        Double value = getValue();
        if (type == 0) value *= -1.00;
        String category = categorySpinner.getSelectedItem().toString();
        String account = accountSpinner.getSelectedItem().toString();

        AccountDAO accountDB = new AccountDAO(this);
        CategoryDAO categoryDB = new CategoryDAO(this);
        accountDB.open();
        categoryDB.open();

        int categoryID, accountID;

        Cursor categoryC = categoryDB.selectCategoryIDFrom(category);
        Cursor accountC = accountDB.selectAccountIDFrom(account);

        categoryID = categoryC.getInt(categoryC.getColumnIndex(DBHelper.KEY_CATEGORY_ID));
        accountID = accountC.getInt(accountC.getColumnIndex(DBHelper.KEY_ACCOUNT_ID));

        // opening database
        dbTransaction.open();
        // insert data into table
        dbTransaction.insertData(type, desc, value, date, categoryID, accountID);

        updateAccountValue(account);
    }

    /**
     * Updates the transaction account's current balance by adding this transaction's value to it.
     *
     * @param account the account name associated with the transaction.
     */
    public void updateAccountValue(String account) {
        Cursor totalTransCursor = dbTransaction.getTransValueData(account);
        double totalBalance = totalTransCursor.getDouble(totalTransCursor.getColumnIndex("totalBalance"));
        dbAccount.open();
        dbAccount.updateAccountBalance(totalBalance, account);
    }

    /**
     * Loads all categories inside the Category dropdown for AddTransaction.
     */
    private void loadSpinnerDataCategory() {
        ArrayList<String> category = new ArrayList<>();
        Cursor c = dbCategory.selectAll();
        c.moveToFirst();

        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(DBHelper.KEY_CATEGORY_NAME));
            category.add(name);
            c.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                category);

        categorySpinner.setAdapter(adapter);
        dbCategory.close();
    }

    /**
     * Loads all accounts inside the Account dropdown for AddTransaction.
     */
    private void loadSpinnerDataAccount() {
        ArrayList<String> account = new ArrayList<>();
        Cursor c = dbAccount.selectAll();
        c.moveToFirst();

        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME));
            account.add(name);
            c.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                account);

        accountSpinner.setAdapter(adapter);
        dbAccount.close();
    }

    /**
     * Creates an intent to "Add Category", and starts this activity.
     *
     * @param item the menu option responsible for calling AddCategoryActivity.
     */
    public void addCategory(MenuItem item) {
        startActivity(new Intent(this, AddCategoryActivity.class));
    }

    /**
     * Goes back to the main activity.
     *
     * @return an intent to MainActivity.
     */
    public Intent goBackToMainActivity() {
        return new Intent(this, MainActivity.class);
    }

    /**
     * Starts a new "Add Account" activity and sets it to come back to this activity.
     *
     * @param item the menu item that called this method.
     */
    public void createAccount(MenuItem item) {
        Intent createAccIntent = new Intent(this, AddAccountActivity.class);
        createAccIntent.putExtra("original", "AddAccountActivity");
        startActivity(new Intent(this, AddAccountActivity.class));
    }

    /**
     * In order to always come back to MainActivity.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }


}