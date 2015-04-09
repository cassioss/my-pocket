package edu.illinois.dscs.mypocket;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

    private double getValue() {
        EditText transactionValue = (EditText) findViewById(R.id.value_entry);
        return Double.valueOf(transactionValue.getText().toString());
    }

    private String getDescription() {
        EditText description = (EditText) findViewById(R.id.description_entry);
        return description.getText().toString();
    }

}
