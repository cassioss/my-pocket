package edu.illinois.dscs.mypocket;

import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.illinois.dscs.mypocket.model.Account;
import edu.illinois.dscs.mypocket.model.Category;
import edu.illinois.dscs.mypocket.model.Transaction;
import edu.illinois.dscs.mypocket.model.TransactionType;

/**
 * @author Cassio
 * @version 1.0
 */
public class AddTransactionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        String[] categories = {"No category"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        Spinner accountSpinner = (Spinner) findViewById(R.id.account_spinner);
        String[] accounts = {"MyPocket"};
        ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accounts);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(accountAdapter);

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
        Intent goBackToMain = new Intent(this, MainActivity.class);
        Transaction newTransaction = new Transaction(getTransactionChoice(), getDescription(), getValue(), getDate(), getCategory(), getAccount());
        MainActivity.lastTransactions.add(newTransaction);
        startActivity(goBackToMain);
    }

    private TransactionType getTransactionChoice() {
        TransactionType thisType = TransactionType.EXPENSE;
        RadioGroup choiceGroup = (RadioGroup) findViewById(R.id.transaction_choice_radio_group);
        int radioButtonID = choiceGroup.getCheckedRadioButtonId();
        switch (radioButtonID) {
            case R.id.expense_radio:
                break;
            case R.id.income_radio:
                thisType = TransactionType.INCOME;
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

    private Date getDate() {
        EditText dateView = (EditText) findViewById(R.id.date_field);
        String dateText = dateView.getText().toString();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date myDate = null;
        try {
            myDate = df.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myDate;
    }

    private Account getAccount() {
        return MainActivity.myPocket;
    }

    private Category getCategory() {
        return MainActivity.noCategory;
    }

}
