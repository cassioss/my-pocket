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
import android.widget.Toast;

import java.text.NumberFormat;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.dao.TransactionDAO;

/**
 * MyPocket's first (main) screen.
 *
 * @author Cassio, Dennis
 * @version 1.3
 * @since 1.2
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
        Intent transactionIntent = new Intent(this, AddTransactionActivity.class);
        transactionIntent.putExtra("CallingActivity", "MainActivity");
        startActivity(transactionIntent);
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

        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int getIndex = cursor.getColumnIndex(DBHelper.KEY_TRANS_VALUE);
                System.out.println(getIndex);
                String value = cursor.getString(getIndex);
                TextView tv = (TextView) view.findViewById(R.id.transaction_value_text_view);
                if (tv != null) {
                    tv.setText(moneyWithTwoDecimals(value));
                    tv.setTextColor(setMoneyColor(value));
                    return true;
                } else return false;
            }
        });

        lastEntries.setAdapter(myCursorAdapter);
    }

    /**
     * Given a string that contains a double value, returns a string with the correct format for currency.
     *
     * @param doubleValue string that corresponds to a double.
     * @return "$ " followed by the money sign (if any) and a number with commas for thousands and two decimal digits.
     */
    public String moneyWithTwoDecimals(String doubleValue) {
        double parsedValue = Double.valueOf(doubleValue);
        return "$ " + NumberFormat.getCurrencyInstance().format(parsedValue).replaceAll("[$ ]", "").replaceAll("^-(?=0(.00*)?$)", "");
    }

    /**
     * Returns an integer that corresponds to a color for a given (money) value contained in a string.
     *
     * @param stringValue a string that contains a (money) value.
     * @return red if positive, green if negative, black if exactly 0.00
     */
    public int setMoneyColor(String stringValue) {
        Double doubleValue = Double.valueOf(moneyWithTwoDecimals(stringValue).replaceAll("[$, ]", ""));
        if (doubleValue > 0.00)
            return Color.argb(255, 0, 200, 0);  // Green
        else if (doubleValue < 0.00)
            return Color.RED;
        else
            return Color.BLACK;
    }

    /**
     * Creates a CSV file that contains all user inputs so far.
     *
     * @param item menu item that called the backup function (not being used here).
     */
    public void saveAsCSV(MenuItem item) {
        Toast toast = Toast.makeText(getApplicationContext(), "Saving data...", Toast.LENGTH_SHORT);
        toast.show();
    }
}
