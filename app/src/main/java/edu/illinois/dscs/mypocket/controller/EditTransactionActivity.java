package edu.illinois.dscs.mypocket.controller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import edu.illinois.dscs.mypocket.R;
import edu.illinois.dscs.mypocket.dao.DBHelper;
import edu.illinois.dscs.mypocket.dao.TransactionDAO;

/**
 * @author Cassio
 * @version 1.0
 */
public class EditTransactionActivity extends ActionBarActivity {

    TransactionDAO transDB;
    String transName;
    EditText transDescription;
    EditText transValue;
    EditText transDate;
    Spinner transCategory;
    Spinner transAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

    transDescription = (EditText) findViewById(R.id.edit_transaction_description);
    transValue = (EditText) findViewById(R.id.edit_transaction_value);
    transDate = (EditText) findViewById(R.id.edit_transaction_date);
    transCategory = (Spinner) findViewById(R.id.edit_transaction_category);
    transAccount = (Spinner) findViewById(R.id.edit_transaction_account);

    transDB = new TransactionDAO(this);

    Intent calledIntent = getIntent();
    transName = calledIntent.getStringExtra("transName");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_transaction, menu);
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

    public void updateTransaction(View view) {
    }

    public void deleteTransaction(View view) {
    }

    public void fillTransactionInfo(){
        transDB.open();
        Cursor c = transDB.selectTrans(transName);

        while (!c.isAfterLast()) {
            transDescription.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TRANS_DESCRIPTION)));
            transValue.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TRANS_VALUE)));
            transDate.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TRANS_CREATION_DATE)));
            String category = c.getString(c.getColumnIndex((DBHelper.KEY_CATEGORY_ID)));
            String account = c.getString(c.getColumnIndex(DBHelper.KEY_ACCOUNT_ID));
            c.moveToNext();
        }



    }
}
