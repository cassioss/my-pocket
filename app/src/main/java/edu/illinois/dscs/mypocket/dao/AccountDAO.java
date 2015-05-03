package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    ////////////////////
    // SELECT queries //
    ////////////////////

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
     * Gets an account's ID based on its name.
     *
     * @param accountName the account name.
     * @return a Cursor object containing this account's ID.
     */
    public Cursor selectAccountIDFrom(String accountName) {
        Cursor c = database.rawQuery("SELECT accountID FROM account WHERE accountName LIKE '" + accountName + "';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Gets the account names and current balances of all the accounts inside the Accounts table.
     * Since the result will be used inside a cursor adapter, the account ID is also necessary.
     *
     * @return a Cursor object containing the data brought from the query.
     */
    public Cursor selectNameAndBalance() {
        Cursor c = database.rawQuery("SELECT accountID AS _id, accountName, currentBalance FROM Account", null);
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
        Cursor c = database.rawQuery("SELECT initialValue FROM Account WHERE accountName LIKE '" + accountName + "';", null);
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
        Cursor c = database.rawQuery("SELECT currentBalance FROM Account WHERE accountName LIKE '" + accountName + "';", null);
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
     * Gets an account's ACTIVE status based on its name.
     *
     * @param accountName the account name.
     * @return a Cursor object containing this account's ACTIVE status.
     */
    public Cursor selectAccountActiveFrom(String accountName) {
        Cursor c = database.rawQuery("SELECT accountActive FROM account WHERE accountName LIKE '" + accountName + "';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    //////////////////
    // INSERT query //
    //////////////////

    /**
     * Inserts a new Account object inside the database, based on its attributes.
     *
     * @param accountName   the account name.
     * @param initialValue  the account's initial value.
     * @param currentBalace the account's current balance (equal to the initial value at the beginning).
     * @param accountActive the account's status (active, 1, or not, 0).
     */
    public void insertNewAccount(String accountName, double initialValue, double currentBalace, int accountActive) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_ACCOUNT_NAME, accountName);
        cv.put(DBHelper.KEY_ACCOUNT_INITIAL_VALUE, initialValue);
        cv.put(DBHelper.KEY_ACCOUNT_CURRENT_BALANCE, currentBalace);
        cv.put(DBHelper.KEY_ACCOUNT_ACTIVE, accountActive);
        database.insert(DBHelper.TABLE_ACCOUNT, null, cv);
    }

    ////////////////////
    // UPDATE queries //
    ////////////////////

    /**
     * Updates an account's current balance.
     *
     * @param newBalance  the account's new balance.
     * @param accountName the account name.
     */
    public void updateAccountBalance(double newBalance, String accountName) {
        database.execSQL("UPDATE Account SET currentBalance = " + newBalance + " WHERE accountName LIKE '" + accountName + "';");
    }

    /**
     * Updates the ACTIVE status of an account.
     *
     * @param newActive   the new ACTIVE status (1 if active, 0 otherwise).
     * @param accountName the account name.
     */
    public void updateAccountActive(int newActive, String accountName) {
        database.execSQL("UPDATE Account SET accountActive = " + newActive + " WHERE accountName LIKE '" + accountName + "';");
    }
}
