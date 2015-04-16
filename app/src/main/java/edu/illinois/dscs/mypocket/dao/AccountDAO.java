package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.illinois.dscs.mypocket.model.Account;

/**
 * Created by denniscardoso on 4/16/15.
 */
public class AccountDAO {

    private SQLiteDatabase database;
    private DatabaseHandler dbHandler;
    private String[] allAccount = {DatabaseHandler.KEY_ACCOUNT_ID,
                                   DatabaseHandler.KEY_ACCOUNT_NAME,
                                   DatabaseHandler.KEY_INITIAL_VALUE,
                                   DatabaseHandler.KEY_CURRENT_BALANCE,
                                   DatabaseHandler.KEY_ACCOUNT_ACTIVE};

    /**
     * DAO Constructor for transactions.
     *
     * @param context the database context in the phone.
     */
    public AccountDAO(Context context) {
        dbHandler = new DatabaseHandler(context);
    }

    /**
     * Opens the database handler.
     *
     * @throws java.sql.SQLException if the database cannot be reached.
     */
    public void open() throws SQLException {
        database = dbHandler.getWritableDatabase();
    }

    /**
     * Closes the database handler.
     */
    public void close() {
        dbHandler.close();
    }

    /**
     * Creates a Transaction object from all the data obtained from user interactions,
     * inserting it into the database.
     *
     * @param type         an integer associated with the transaction type (expense or income).
     * @param description  the transaction description.
     * @param value        the transaction value (always non-negative).
     * @param creationDate the transaction's creation date (not necessarily today).
     * @param categoryID   the ID of the category associated with the transaction.
     * @param accountID    the ID of the account that contains the transaction.
     * @return a Transaction object whose information is already inside the database.
     */
    public Account createAccount(String accountName, double initialValue,  double currentBalance,
                                         int accountActive) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.KEY_ACCOUNT_NAME, accountName);
        values.put(DatabaseHandler.KEY_INITIAL_VALUE, initialValue);
        values.put(DatabaseHandler.KEY_CURRENT_BALANCE, currentBalance);
        values.put(DatabaseHandler.KEY_ACCOUNT_ACTIVE, accountActive);
        long insertId = database.insert(DatabaseHandler.TABLE_ACCOUNT, null,
                values);
        Cursor cursor = database.query(DatabaseHandler.TABLE_ACCOUNT,
                allAccount, DatabaseHandler.KEY_ACCOUNT_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Account newAccount = cursorToAccount(cursor);
        cursor.close();
        return newAccount;
    }

    /**
     * Deletes a given transaction from the database.
     *
     * @param transaction the transaction marked for deletion.
     */
    public void deleteAccount(Account account) {
        long id = account.getAccountID();
        System.out.println("Account deleted with id: " + id);
        database.delete(DatabaseHandler.TABLE_ACCOUNT, DatabaseHandler.KEY_ACCOUNT_ID
                + " = " + id, null);
    }

    /**
     * Gets all transactions from the database.
     *
     * @return a list with all items from the Transactions table turned into Transaction objects.
     */
    public List<Account> getAllTransactions() {
        List<Account> accounts = new ArrayList<Account>();

        Cursor cursor = database.query(DatabaseHandler.TABLE_ACCOUNT,
                allAccount, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Account account = cursorToAccount(cursor);
            accounts.add(account);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return accounts;
    }

    /**
     * Turns a cursor item into a full transaction.
     *
     * @param cursor the cursor with a given transaction in the database.
     * @return a transaction equivalent to the one pointed by the cursor.
     */
    private Account cursorToAccount(Cursor cursor) {
        Account account = new Account(0, null, 0.0, true);
        account.setAccountID (cursor.getInt(0));
        account.setName (cursor.getString(1));
        account.getInitialValue (cursor.getDouble(2));
        account.getCurrentBalance(cursor.getDouble(3));
        account.isAccountActive (cursor.getInt(4));
        return account;
    }

}
