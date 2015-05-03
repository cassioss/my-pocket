package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.dscs.mypocket.model.Account;

/**
 * Data access object for the Account table.
 *
 * @author Cassio, Dennis
 * @version 1.3
 * @since 1.2
 */
public class AccountDAO {

    protected SQLiteDatabase database;
    protected DBHelper dbHandler;
    protected Context Context;

    private String[] allAccounts = {DBHelper.KEY_ACCOUNT_ID,
            DBHelper.KEY_ACCOUNT_NAME,
            DBHelper.KEY_ACCOUNT_INITIAL_VALUE,
            DBHelper.KEY_ACCOUNT_CURRENT_BALANCE,
            DBHelper.KEY_ACCOUNT_ACTIVE};

    /**
     * DAO constructor for accounts.
     *
     * @param context the database context in the phone.
     */
    public AccountDAO(Context context) {
        Context = context;
    }

    // Open/close database functions

    /**
     * Opens the database.
     *
     * @return itself (the object instance of AccountDAO calling the method).
     */
    public AccountDAO open() {
        dbHandler = new DBHelper(Context);
        database = dbHandler.getWritableDatabase();
        return this;
    }

    /**
     * Closes the database.
     */
    public void close() {
        dbHandler.close();
    }

    // SELECT queries

    /**
     * Gets all the rows inside the Account table, equivalent to: SELECT * from Account;
     *
     * @return a Cursor object containing the data brought from the query.
     */
    public Cursor selectAll() {
        Cursor c = database.query(DBHelper.TABLE_ACCOUNT, allAccounts, null, null, null, null, null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Gets the account names and current balances of all the accounts inside the Accounts table. Since the result will be used inside a cursor adapter, the account ID is also necessary.
     *
     * @return a Cursor object containing the data brought from the query.
     */
    public Cursor selectNameAndBalance() {
        Cursor c = database.rawQuery("SELECT accountID AS _id, accountName, currentBalance FROM Account", null);
        //Cursor c = database.query(DBHelper.TABLE_ACCOUNT, listAccounts, null, null, null, null, null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Gets the total balance as a sum of all account's current balances.
     *
     * @return a Cursor object containing the result of this sum.
     */
    public Cursor selectTotalBalance() {
        Cursor c = database.rawQuery("SELECT SUM(currentBalance) AS currentBalance FROM Account WHERE accountActive = 1;", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Gets an account's initial value based on its name.
     *
     * @param accountName the account name.
     * @return a Cursor object containing the initial value of this account.
     */
    public Cursor selectInitialValueFrom(String accountName) {
        Cursor c = database.rawQuery("SELECT initialValue FROM Account WHERE accountName LIKE'" + accountName + "';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Gets an account's current balance based on its name.
     *
     * @param accountName the account name.
     * @return a Cursor object containing the current balance of this account.
     */
    public Cursor selectCurrentBalanceFrom(String accountName) {
        Cursor c = database.rawQuery("SELECT currentBalance FROM Account WHERE accountName LIKE'" + accountName + "';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    public void insertData(String name, double initialValue, double CurrentValue, int accountActive) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_ACCOUNT_NAME, name);
        cv.put(DBHelper.KEY_ACCOUNT_INITIAL_VALUE, initialValue);
        cv.put(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE, CurrentValue);
        cv.put(DBHelper.KEY_ACCOUNT_ACTIVE, accountActive);

        database.insert(DBHelper.TABLE_ACCOUNT, null, cv);
    }

    public Cursor getAccountId(String name) {
        Cursor c = database.rawQuery("select accountID from account WHERE accountName like '" + name + "';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    public Cursor getAccountActive(String name) {
        Cursor c = database.rawQuery("select accountActive from account WHERE accountName like '" + name + "';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    public void updateAccountValue(double total, String account) {
        database.execSQL("Update account set currentBalance = " + total + " where accountName like '" + account + "';");
    }

    public void updateAccountActive(int newActive, String account) {
        database.execSQL("Update account set accountActive = " + newActive + " where accountName like '" + account + "';");
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
        values.put(DBHelper.KEY_ACCOUNT_NAME, accountName);
        values.put(DBHelper.KEY_ACCOUNT_INITIAL_VALUE, initialValue);
        values.put(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE, currentBalance);
        values.put(DBHelper.KEY_ACCOUNT_ACTIVE, accountActive);
        long insertId = database.insert(DBHelper.TABLE_ACCOUNT, null,
                values);
        Cursor cursor = database.query(DBHelper.TABLE_ACCOUNT,
                allAccounts, DBHelper.KEY_ACCOUNT_ID + " = " + insertId, null,
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
        database.delete(DBHelper.TABLE_ACCOUNT, DBHelper.KEY_ACCOUNT_ID
                + " = " + id, null);
    }

    /**
     * Gets all account from the database.
     *
     * @return a list with all items from the Account table turned into Account objects.
     */
    public List<Account> getAllAcounts() {
        List<Account> accounts = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TABLE_ACCOUNT,
                allAccounts, null, null, null, null, null);

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
