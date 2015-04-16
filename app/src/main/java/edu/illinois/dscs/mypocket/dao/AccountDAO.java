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
 * @author Dennis
 * @version 1.0
 */
public class AccountDAO extends BasicDAO {

    private String[] allAccount = {DatabaseHandler.KEY_ACCOUNT_ID,
            DatabaseHandler.KEY_ACCOUNT_NAME,
            DatabaseHandler.KEY_INITIAL_VALUE,
            DatabaseHandler.KEY_CURRENT_BALANCE,
            DatabaseHandler.KEY_ACCOUNT_ACTIVE};

    /**
     * DAO constructor for accounts.
     *
     * @param context the database context in the phone.
     */
    public AccountDAO(Context context) {
        super(context);
    }

    /**
     * Creates an Account object from all the data obtained from user interactions,
     * inserting it into the database.
     *
     * @param accountName    the account name.
     * @param initialValue   the account's initial value.
     * @param currentBalance the account's current balance.
     * @param accountActive  an integer associated with the account being active or not.
     * @return an Account object whose information is already inside the database.
     */
    public Account createAccount(String accountName, double initialValue, double currentBalance,
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
     * Deletes a given account from the database.
     *
     * @param account the account marked for deletion.
     */
    public void deleteAccount(Account account) {
        long id = account.getAccountID();
        System.out.println("Account deleted with id: " + id);
        database.delete(DatabaseHandler.TABLE_ACCOUNT, DatabaseHandler.KEY_ACCOUNT_ID
                + " = " + id, null);
    }

    /**
     * Gets all account from the database.
     *
     * @return a list with all items from the Account table turned into Account objects.
     */
    public List<Account> getAllAcounts() {
        List<Account> accounts = new ArrayList<>();

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
     * Turns a cursor item into a full account.
     *
     * @param cursor the cursor with a given account in the database.
     * @return an account equivalent to the one pointed by the cursor.
     */
    private Account cursorToAccount(Cursor cursor) {
        Account account = new Account(0, null, 0.0, true);
        account.setAccountID(cursor.getInt(0));
        account.setName(cursor.getString(1));
        account.setInitialValue(cursor.getDouble(2));
        account.setCurrentBalance(cursor.getDouble(3));
        account.setAccountActive(cursor.getInt(4) > 0);
        return account;
    }

}
