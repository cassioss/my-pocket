package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.CategoryDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.dao.TransactionDAO;
import edu.illinois.dscs.mypocket.model.SaveAsExcel;
import edu.illinois.dscs.mypocket.utils.CurrencyUtils;
import edu.illinois.dscs.mypocket.utils.ValidationUtils;
import jxl.write.WriteException;

/**
 * MyPocket's first (main) screen.
 *
 * @author Cassio, Dennis
 * @version 1.3
 * @since 1.2
 */
public class MainActivity extends ActionBarActivity {

    private TransactionDAO transDB;
    private AccountDAO accountDB;
    private CategoryDAO categoryDB;
    private ListView lastEntries;
    private TextView totalBalanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transDB = new TransactionDAO(this);
        transDB.open();

        accountDB = new AccountDAO(this);
        accountDB.open();

        categoryDB = new CategoryDAO(this);
        categoryDB.open();

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
        totalBalanceText.setText(CurrencyUtils.moneyWithTwoDecimals(totalBalance));
        totalBalanceText.setTextColor(CurrencyUtils.setMoneyColor(totalBalance));
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
                String value = cursor.getString(getIndex);
                TextView tv = (TextView) view.findViewById(R.id.transaction_value_text_view);
                if (tv != null) {
                    tv.setText(CurrencyUtils.moneyWithTwoDecimals(value));
                    tv.setTextColor(CurrencyUtils.setMoneyColor(value));
                    return true;
                } else return false;
            }
        });

        lastEntries.setAdapter(myCursorAdapter);
    }

    /**
     * Creates a CSV file that contains all user inputs so far.
     *
     * @param item menu item that called the backup function (not being used here).
     */
    public void saveAsCSV(MenuItem item) throws WriteException, IOException {
        SaveAsExcel myPocketExcel = new SaveAsExcel();
        myPocketExcel.setOutputFile("MyPocket");
        myPocketExcel.setCursors(transDB.completeTransData(), accountDB.readData(), categoryDB.readData());
        ValidationUtils.makeToast(getApplicationContext(), "Saving data to Download/MyPocket.xls ...");
        myPocketExcel.write();
        broadcastSave(myPocketExcel.getFile(), "Data saved successfully!");
    }

    /**
     * Sends a message to inform that a new file has been created.
     *
     * @param file the File object being created.
     */

    private void broadcastSave(File file, String message) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
        ValidationUtils.makeToast(getApplicationContext(), message);
    }
}
