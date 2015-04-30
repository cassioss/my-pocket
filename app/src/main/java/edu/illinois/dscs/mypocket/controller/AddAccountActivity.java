package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.text.NumberFormat;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.utils.ValidationUtils;

/**
 * @author Cassio, Dennis
 * @version 1.1
 * @since 1.0
 */
public class AddAccountActivity extends ActionBarActivity {

    AccountDAO dbAccount;
    EditText value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        dbAccount = new AccountDAO(this);
        value = (EditText) findViewById(R.id.account_initial_Value_edit_view);
        value.addTextChangedListener(new CurrencyTextWatcher());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
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

    /*
    public void saveAccount(View view) {
        Intent goBackToShowAccount = new Intent(this, ShowAccountsActivity.class);
        //Account newAccount = new Account(getName(), getInitialValue(), true);
        //ShowAccountsActivity.showAccounts.add(newAccount);
        startActivity(goBackToShowAccount);
    }*/

    public String getName() {
        EditText accountNameView = (EditText) findViewById(R.id.account_name_edit_view);
        return accountNameView.getText().toString();
    }

    public double getInitialValue() {
        String cleanValue = value.getText().toString().replaceAll("[$,.]", "");
        double doubleValue100Times = Double.parseDouble(cleanValue);
        return doubleValue100Times / 100.0;
    }

    public int getAccountActive() {
        RadioGroup choiceGroup = (RadioGroup) findViewById(R.id.account_choice_radio_group);
        int radioButtonID = choiceGroup.getCheckedRadioButtonId();
        int ActiveValue = 1;

        switch (radioButtonID) {
            case R.id.account_active_yes_radio:
                break;
            case R.id.account_active_no_radio:
                ActiveValue = 0;
        }
        return ActiveValue;
    }

    /**
     * Inserts all user inputs into the Account table.
     */
    private void insertAccountData() {
        String desc = getName();
        double initialValue = getInitialValue();
        int type = getAccountActive();
        dbAccount.open();                                               // Opens database
        dbAccount.insertData(desc, initialValue, initialValue, type);   // Inserts data into table
    }

    /**
     * Returns to the activity that called this one. This is required to recreate any
     * database-related information, and consider the new account on them.
     *
     * @return an intent to the original activity.
     */
    public Intent goBack() {
        Intent calledIntent = getIntent();
        //String activityFromIntent = calledIntent.getStringExtra("original");
        return new Intent(this, AddTransactionActivity.class);
    }

    /**
     * Inserts all data inserted by the user into the Account table as a new Account.
     *
     * @param view the View object (button) that called this method.
     */
    public void saveAccount(View view) {
        if (ValidationUtils.invalidName(getApplicationContext(), getName())) return;
        insertAccountData();
        startActivity(goBack());
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
                String formatted = NumberFormat.getCurrencyInstance().format(parsed / 100.0);

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
