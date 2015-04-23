package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.model.Account;
import edu.illinois.dscs.mypocket.model.AccountAdapter;

public class ShowAccountsActivity extends ActionBarActivity {


    AccountDAO db = new AccountDAO(this);
    //public static Account myPocket = new Account(1, "MyPocket", 0.00, true);
    //public static ArrayList<Account> showAccounts = new ArrayList<>();
    ListAdapter showAccountAdapter;
    ListView showAccountList;
    AccountDAO dbAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_accounts);

        //if (!showAccounts.contains(myPocket))
        //    showAccounts.add(myPocket);

        //showAccountAdapter = new AccountAdapter(this, showAccounts);

        showAccountList = (ListView) findViewById(R.id.showAccountList);

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
        getMenuInflater().inflate(R.menu.menu_show_accounts, menu);
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

        AddAccountIntent.putExtra("CallActivity", "ShowAccountsActivity");
        startActivity(AddAccountIntent);

    }

    public void loadAccountList(){
        ArrayList<Account> showAccounts = new ArrayList<>();
        Account accountObj = new Account(0,null,0.0,true);
        Cursor c = db.readData();

        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_NAME));
            Double currentValue = c.getDouble(c.getColumnIndex(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE));
            accountObj.setName(name);
            accountObj.setCurrentBalance(currentValue);
            showAccounts.add(accountObj);
            c.moveToNext();
        }
    }


}
