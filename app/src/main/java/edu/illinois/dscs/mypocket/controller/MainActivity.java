package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.model.Transaction;
import edu.illinois.dscs.mypocket.model.TransactionAdapter;
import edu.illinois.dscs.mypocket.model.TransactionType;

public class MainActivity extends ActionBarActivity {

    public static ArrayList<Transaction> lastTransactions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListAdapter entriesAdapter = new TransactionAdapter(this, lastTransactions);
        ListView lastEntries = (ListView) findViewById(R.id.last_entries_list_view);
        lastEntries.setAdapter(entriesAdapter);

        double totalValue = getTotalBalance();

        TextView totalBalance = (TextView) findViewById(R.id.total_balance_value_text_view);
        double module = Math.abs(totalValue);
        String moduleText = String.format("%.2f", module);

        if (totalValue < 0.00) {
            totalBalance.setTextColor(Color.RED);
            totalBalance.setText("-US$ " + moduleText);
        } else {
            totalBalance.setText("US$ " + moduleText);
            if (totalValue > 0.00)
                totalBalance.setTextColor(Color.GREEN);
            else
                totalBalance.setTextColor(Color.BLACK);
        }

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

    private double getTotalBalance() {
        double totalValue = 0.00;
        for (Transaction transaction : lastTransactions) {
            if (transaction.getType() == TransactionType.EXPENSE)
                totalValue -= transaction.getValue();
            else if (transaction.getType() == TransactionType.INCOME)
                totalValue += transaction.getValue();
        }
        return totalValue;
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
