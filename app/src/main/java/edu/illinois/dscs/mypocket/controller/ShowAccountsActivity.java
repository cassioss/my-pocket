package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.AccountDAO;
import edu.illinois.dscs.mypocket.dao.DBHelper;

public class ShowAccountsActivity extends ActionBarActivity {

    AccountDAO db;
    ListView showAccountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_accounts);
        db = new AccountDAO(this);
        db.open();
        loadAccountList();
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

    public void loadAccountList() {
        Cursor c = db.readDataList();
        String[] fromFieldNames = new String[]{DBHelper.KEY_ACCOUNT_NAME, DBHelper.KEY_ACCOUNT_CURRENT_BALANCE};
        int[] toViewIDs = new int[]{R.id.account_name_account_button, R.id.account_value_text_view};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.show_account_row_layout, c, fromFieldNames, toViewIDs, 0);
        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int getIndex = cursor.getColumnIndex(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE);
                System.out.println(getIndex);
                String value = cursor.getString(getIndex);
                TextView tv = (TextView) view.findViewById(R.id.account_value_text_view);
                MainActivity mActivity = new MainActivity();
                if (tv != null) {
                    tv.setText(mActivity.moneyWithTwoDecimals(value));
                    tv.setTextColor(mActivity.setMoneyColor(value));
                    return true;
                } else return false;
            }
        });
        showAccountList = (ListView) findViewById(R.id.showAccountList);
        showAccountList.setAdapter(myCursorAdapter);
    }

    /**
     * In order to always come back to MainActivity.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
