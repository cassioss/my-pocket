package edu.illinois.dscs.mypocket.controller;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_account_trans);

        transDB = new TransactionDAO(this);
        transDB.open();

        accountDB = new AccountDAO(this);
        accountDB.open();

        currentBalanceTextView = (TextView) findViewById(R.id.current_balance_value_text_view);
        allEntries = (ListView) findViewById(R.id.all_entries_list_view);

        loadCurrentBalance();
        loadAccountTransactions();

        allEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void loadCurrentBalance() {

    }

    private void loadAccountTransactions() {

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
