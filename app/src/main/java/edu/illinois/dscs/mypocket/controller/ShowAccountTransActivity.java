package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.dao.TransactionDAO;

/**
 * @author Cassio
 * @version 1.0
 */
public class ShowAccountTransActivity extends ActionBarActivity {

    TransactionDAO transDB;
    AccountDAO accountDB;
    ListView allEntries;
    TextView currentBalanceTextView;
    TextView initialValueTextView;
    MainActivity mActivity;
    String accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_account_trans);

        transDB = new TransactionDAO(this);
        transDB.open();

        accountDB = new AccountDAO(this);
        accountDB.open();

        mActivity = new MainActivity();

        currentBalanceTextView = (TextView) findViewById(R.id.current_balance_value_text_view);
        initialValueTextView = (TextView) findViewById(R.id.initial_value_text_view);
        allEntries = (ListView) findViewById(R.id.all_entries_list_view);

        Intent calledIntent = getIntent();
        accountName = calledIntent.getStringExtra("accountName");

        loadCurrentBalance();
        loadInitialValue();
        loadAccountTransactions();

        allEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void loadCurrentBalance() {
        Cursor c = accountDB.readCurrentBalance(accountName);
        String currentBalance = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE));
        currentBalanceTextView.setText(mActivity.moneyWithTwoDecimals(currentBalance));
        currentBalanceTextView.setTextColor(mActivity.setMoneyColor(currentBalance));
    }

    private void loadInitialValue() {
        Cursor c = accountDB.readInitialValue(accountName);
        String currentBalance = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_INITIAL_VALUE));
        initialValueTextView.setText(mActivity.moneyWithTwoDecimals(currentBalance));
        initialValueTextView.setTextColor(mActivity.setMoneyColor(currentBalance));
    }

    private void loadAccountTransactions() {
        Cursor c = transDB.readAccountTrans(accountName);
        String[] fromFieldNames = new String[]{DBHelper.KEY_TRANS_DESCRIPTION, DBHelper.KEY_TRANS_VALUE};
        int[] toViewIDs = new int[]{R.id.transaction_name_text_view, R.id.transaction_value_text_view};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.transaction_row_layout, c, fromFieldNames, toViewIDs, 0);

        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int getIndex = cursor.getColumnIndex(DBHelper.KEY_TRANS_VALUE);
                String value = cursor.getString(getIndex);
                TextView tv = (TextView) view.findViewById(R.id.transaction_value_text_view);
                if (tv != null) {
                    MainActivity mActivity = new MainActivity();
                    tv.setText(mActivity.moneyWithTwoDecimals(value));
                    tv.setTextColor(mActivity.setMoneyColor(value));
                    return true;
                } else return false;
            }
        });

        allEntries.setAdapter(myCursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_account_trans, menu);
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
}
