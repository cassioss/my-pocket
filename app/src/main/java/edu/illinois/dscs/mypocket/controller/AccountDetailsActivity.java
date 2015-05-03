package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.dao.TransactionDAO;
import edu.illinois.dscs.mypocket.utils.CurrencyUtils;

/**
 * @author Cassio
 * @version 1.0
 */
public class AccountDetailsActivity extends ActionBarActivity {

    TransactionDAO transDB;
    AccountDAO accountDB;
    ListView allEntries;
    TextView currentBalanceTextView;
    TextView initialValueTextView;
    ToggleButton activateAccount;
    MainActivity mActivity;
    String accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        transDB = new TransactionDAO(this);
        transDB.open();

        accountDB = new AccountDAO(this);
        accountDB.open();

        mActivity = new MainActivity();

        currentBalanceTextView = (TextView) findViewById(R.id.current_balance_value_text_view);
        initialValueTextView = (TextView) findViewById(R.id.initial_value_text_view);
        allEntries = (ListView) findViewById(R.id.all_entries_list_view);
        activateAccount = (ToggleButton) findViewById(R.id.activate_account_toggle_button);

        Intent calledIntent = getIntent();
        accountName = calledIntent.getStringExtra("accountName");

        loadCurrentBalance();
        loadInitialValue();
        loadAccountTransactions();
        loadActivateToggle();

        allEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void loadCurrentBalance() {
        Cursor c = accountDB.readCurrentBalance(accountName);
        Double currentBalance = c.getDouble(c.getColumnIndex(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE));
        String totalBalance = currentBalance.toString();
        currentBalanceTextView.setText(CurrencyUtils.moneyWithTwoDecimals(totalBalance));
        currentBalanceTextView.setTextColor(CurrencyUtils.setMoneyColor(totalBalance));
    }

    private void loadInitialValue() {
        Cursor c = accountDB.readInitialValue(accountName);
        String initialValue = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_INITIAL_VALUE));
        initialValueTextView.setText(CurrencyUtils.moneyWithTwoDecimals(initialValue));
        initialValueTextView.setTextColor(CurrencyUtils.setMoneyColor(initialValue));
    }

    private void loadAccountTransactions() {
        Cursor c = transDB.readAccountTrans(accountName);
        String[] fromFieldNames = new String[]{DBHelper.KEY_TRANS_DESCRIPTION, DBHelper.KEY_TRANS_VALUE};
        int[] toViewIDs = new int[]{R.id.transaction_row_layout_description, R.id.transaction_value_text_view};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.transaction_row_layout, c, fromFieldNames, toViewIDs, 0);

        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int getIndex = cursor.getColumnIndex(DBHelper.KEY_TRANS_VALUE);
                String value = cursor.getString(getIndex);
                TextView tv = (TextView) view.findViewById(R.id.transaction_value_text_view);
                if (tv != null) {
                    tv.setText(CurrencyUtils.moneyWithTwoDecimals(value));
                    tv.setTextColor(CurrencyUtils.setMoneyColor(value));
                    //tv.setTag(getIndex);
                    return true;
                } else return false;
            }
        });

        allEntries.setAdapter(myCursorAdapter);
    }

    private void loadActivateToggle() {
        Cursor c = accountDB.getAccountActive(accountName);
        String isActive = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_ACTIVE));
        boolean checkActive = Integer.valueOf(isActive) > 0;
        activateAccount.setChecked(checkActive);
        if (checkActive)
            activateAccount.setBackgroundColor(Color.RED);
        else
            activateAccount.setBackgroundColor(Color.argb(255, 0, 100, 0)); // Dark green
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_details, menu);
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

    /**
     * In order to always come back to ShowAccounts.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ShowAccountsActivity.class);
        startActivity(intent);
    }

    public void editTransactions(View view) {
        TextView textTransName = (TextView) view;
        String transName = textTransName.getText().toString();
        Intent showTransIntent = new Intent(this, EditTransactionActivity.class);
        showTransIntent.putExtra("transName", transName);
        showTransIntent.putExtra("accountName", accountName);
        startActivity(showTransIntent);
    }

    public void changeActivation(View view) {
        updateAccountStatus();
        Intent showTransIntent = new Intent(this, ShowAccountsActivity.class);
        startActivity(showTransIntent);
    }

    private void updateAccountStatus() {
        Cursor c = accountDB.getAccountActive(accountName);
        String isActive = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_ACTIVE));
        boolean checkActive = Integer.valueOf(isActive) > 0;
        Integer newActive = 0;
        if (!checkActive) newActive = 1;
        accountDB.updateAccountActive(newActive, accountName);
    }
}
