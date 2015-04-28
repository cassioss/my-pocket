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

import java.text.NumberFormat;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.dao.TransactionDAO;

/**
 * @author Cassio, Dennis
 * @version 1.2
 * @since 1.1
 */
public class MainActivity extends ActionBarActivity {

    TransactionDAO transDB;
    AccountDAO accountDB;
    ListView lastEntries;
    TextView totalBalanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transDB = new TransactionDAO(this);
        transDB.open();

        accountDB = new AccountDAO(this);
        accountDB.open();

        lastEntries = (ListView) findViewById(R.id.last_entries_list_view);
        totalBalanceText = (TextView) findViewById(R.id.total_balance_value_text_view);

        loadTotalBalance();
        loadTransactionList();

        lastEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void addTransaction(View view) {
        Intent TransactionIntent = new Intent(this, AddTransactionActivity.class);

        TransactionIntent.putExtra("CallingActivity", "MainActivity");
        startActivity(TransactionIntent);
    }

    public void showAccounts(View view) {
        Intent ShowAccountIntent = new Intent(this, ShowAccountsActivity.class);
        ShowAccountIntent.putExtra("CallingActivity", "MainActivity");
        startActivity(ShowAccountIntent);
    }

    public void loadTotalBalance() {
        Cursor c = accountDB.readTotalBalance();
        String totalBalance = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_INITIAL_VALUE));
        totalBalanceText.setText(moneyWithTwoDecimals(totalBalance));
        totalBalanceText.setTextColor(setMoneyColor(totalBalance));
    }

    public void loadTransactionList() {
        Cursor c = transDB.readDataTransList();
        String[] fromFieldNames = new String[]{DBHelper.KEY_TRANS_DESCRIPTION, DBHelper.KEY_TRANS_VALUE};
        int[] toViewIDs = new int[]{R.id.transaction_name_text_view, R.id.transaction_value_text_view};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.transaction_row_layout, c, fromFieldNames, toViewIDs, 0);
        lastEntries.setAdapter(myCursorAdapter);
    }

    public String moneyWithTwoDecimals(String stringValue) {
        double parsedValue = Double.valueOf(stringValue);
        return "$" + NumberFormat.getCurrencyInstance().format(parsedValue).replaceAll("[$,]", "").replaceAll("^-(?=0(.00*)?$)", "");
    }

    public int setMoneyColor(String stringValue) {
        Double doubleValue = Double.valueOf(moneyWithTwoDecimals(stringValue).replaceAll("[$,]", ""));
        if (doubleValue > 0.00)
            return Color.argb(255, 0, 200, 0);  // Green
        else if (doubleValue < 0.00)
            return Color.RED;
        else
            return Color.BLACK;
    }

}
