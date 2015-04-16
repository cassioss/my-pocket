package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.model.Account;


public class AddAccountActivity extends ActionBarActivity {

    private boolean accountActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
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

    public void saveAccount(View view) {
        Intent goBackToShowAccount = new Intent(this, ShowAccountsActivity.class);
        //Account newAccount = new Account(getName(), getInitialValue(), true);
        //ShowAccountsActivity.showAccounts.add(newAccount);
        startActivity(goBackToShowAccount);
    }

    public String getName() {
        EditText accountNameView = (EditText) findViewById(R.id.account_name_edit_view);
        return accountNameView.getText().toString();
    }

    public double getInitialValue() {
        EditText accountInitialValue = (EditText) findViewById(R.id.account_initial_Value_edit_view);
        return Double.valueOf(accountInitialValue.getText().toString());
    }

    public boolean getAccountActive() {
        RadioGroup choiceGroup = (RadioGroup) findViewById(R.id.account_choice_radio_group);
        int radioButtonID = choiceGroup.getCheckedRadioButtonId();
        boolean ActiveValue = true;

        switch (radioButtonID) {
            case R.id.account_active_yes_radio:
                break;
            case R.id.account_active_no_radio:
                ActiveValue = false;
        }
        return ActiveValue;
    }
}
