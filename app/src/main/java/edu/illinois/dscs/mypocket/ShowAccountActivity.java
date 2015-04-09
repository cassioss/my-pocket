package edu.illinois.dscs.mypocket;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.illinois.dscs.mypocket.model.Account;
import edu.illinois.dscs.mypocket.model.AccountAdapter;
import edu.illinois.dscs.mypocket.model.Category;


public class ShowAccountActivity extends ActionBarActivity {

    public static Account myPocket = new Account("MyPocket", 0.00, true);

    public static ArrayList<Account> showAccounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_account);

        ListAdapter showAccountAdapter = new AccountAdapter(this, showAccounts);

        ListView showAccountList = (ListView) findViewById(R.id.showAccountList);

        showAccountList.setAdapter(showAccountAdapter);

        showAccountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_account, menu);
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

    public void add_account_button(View view) {
        Intent AddAccountIntent = new Intent(this, AddAccountActivity.class);

        AddAccountIntent.putExtra("CallActivity","ShowAccountActivity");
        startActivity(AddAccountIntent);

    }
}
