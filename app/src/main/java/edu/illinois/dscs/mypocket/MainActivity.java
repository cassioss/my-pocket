package edu.illinois.dscs.mypocket;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.illinois.dscs.mypocket.model.Transaction;

public class MainActivity extends ActionBarActivity {

    public static ArrayList<Transaction> lastTransactions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListAdapter entriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lastTransactions);

        ListView lastEntries = (ListView) findViewById(R.id.last_entries_list_view);

        lastEntries.setAdapter(entriesAdapter);

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
        Intent ShowAccountIntent = new Intent(this, ShowAccountActivity.class);

        ShowAccountIntent.putExtra("CallingActivity", "MainActivity");
        startActivity(ShowAccountIntent);

    }
}
