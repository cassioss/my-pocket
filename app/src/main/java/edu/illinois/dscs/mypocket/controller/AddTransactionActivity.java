package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.CategoryDAO;
import edu.illinois.dscs.mypocket.dao.DatabaseHandler;
import edu.illinois.dscs.mypocket.model.Account;
import edu.illinois.dscs.mypocket.model.Category;

/**
 * @author Cassio, Dennis
 * @version 1.1
 * @since 1.0
 */
public class AddTransactionActivity extends ActionBarActivity implements OnItemSelectedListener {

   Spinner categorySpinner;
   Spinner accountSpinner;
   CategoryDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);


        db = new CategoryDAO(this);

        db.open();

        //loadSpinnerData();

        //String[] categories = {"No Category"};
        //ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        //categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //categorySpinner.setAdapter(categoryAdapter);

        //loadSpinnerData();

        accountSpinner = (Spinner) findViewById(R.id.account_spinner);
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
        return ShowAccountsActivity.myPocket;
    }

    private Category getCategory() {
        return null;
    }

    private void loadSpinnerData() {
        Cursor c = db.readData();
        ArrayList<String> category = new ArrayList<String>();

        c.moveToFirst();

        while (!c.isAfterLast()) {

            String name = c.getString(c.getColumnIndex(DatabaseHandler.KEY_CATEGORY_NAME));
            category.add(name);
            c.moveToNext();
        }

        ArrayAdapter<String> aa1 = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, R.id.textView1,
                category);

        categorySpinner.setAdapter(aa1);

        db.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}