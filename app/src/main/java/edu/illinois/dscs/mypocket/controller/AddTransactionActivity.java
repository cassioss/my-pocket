package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    private EditText date;
    private EditText value;

    private String current = "";
    private Calendar cal = Calendar.getInstance();

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
        date = (EditText) findViewById(R.id.date_field);
        date.addTextChangedListener(new DateTextWatcher());
        value = (EditText) findViewById(R.id.value_entry);
        value.addTextChangedListener(new CurrencyTextWatcher());
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
        startActivity(goBackToMainActivity());
    }

    public Intent goBackToMainActivity() {
        return new Intent(this, MainActivity.class);
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
        String desc = getDescription();
        int type = getTransactionChoice();
        String date = getDate();
        Double value = getValue();
        String category = categorySpinner.getSelectedItem().toString();
        String account = accountSpinner.getSelectedItem().toString();

        // opening database
        dbTransaction.open();
        // insert data into table
        dbTransaction.insertData(type, desc, value, date, 1, 1);
    }

    /**
     * Loads all categories inside the Category dropdown for AddTransaction.
     */
    private void loadSpinnerDataCategory() {
        ArrayList<String> category = new ArrayList<>();
        Cursor c = dbCategory.readData();
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
        Cursor c = dbAccount.readData();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void createCategory(MenuItem item) {
        startActivity(new Intent(this, AddCategoryActivity.class));
    }

    public void createAccount(MenuItem item) {
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

    /**
     * Private class that uses a TextWatcher specifically for date. Format MM/DD/YYYY.
     *
     * @author Cassio
     * @version 1.0
     */
    private class DateTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    String mmddyyyy = "MMDDYYYY";
                    clean = clean + mmddyyyy.substring(clean.length());
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int mon = Integer.parseInt(clean.substring(0, 2));
                    int day = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    if (mon > 12) mon = 12;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", mon, day, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                date.setText(current);
                date.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * Private class that uses a TextWatcher specifically for currency. Format $#.##
     *
     * @author Cassio
     * @version 1.0
     */
    private class CurrencyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        private String current = "";

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                value.removeTextChangedListener(this);

                String cleanString = s.toString().replaceAll("[$,.]", "");

                double parsed = Double.parseDouble(cleanString);
                String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));

                current = formatted;
                value.setText(formatted);
                value.setSelection(formatted.length());

                value.addTextChangedListener(this);
            }
        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}