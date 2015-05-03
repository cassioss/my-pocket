package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Data Access Object (DAO) for table Transactions, which also queries Category and Account tables
 * in order to obtain foreign keys.
 *
 * @author Cassio, Dennis
 * @version 1.0
 */
public class TransactionDAO {

    protected SQLiteDatabase database;
    protected DBHelper dbHandler;
    protected Context Context;

    /**
     * DAO constructor for transactions.
     *
     * @param context the database context in the phone.
     */
    public TransactionDAO(Context context) {
        Context = context;
    }

    /**
     * Opens the database.
     *
     * @return itself (the object instance of TransactionDAO calling the method).
     */
    public TransactionDAO open() {
        dbHandler = new DBHelper(Context);
        database = dbHandler.getWritableDatabase();
        return this;
    }

    ////////////////////
    // SELECT queries //
    ////////////////////

    /**
     * Selects all the rows of the Transaction table, replacing accountID and categoryID with their names.
     * In order to do that, we need to use INNER JOIN.
     */
    public Cursor completeTransData() {
        Cursor c = database.rawQuery("SELECT " +
                "t.transactionID AS transactionID, " +
                "t.transType AS transType, " +
                "t.description AS description, " +
                "t.transactionValue AS transactionValue, " +
                "t.creationDate AS creationDate, " +
                "a.accountName AS accountName, " +
                "c.categoryName AS categoryName " +
                "FROM Transactions t " +
                "INNER JOIN Account a ON t.accountID = a.accountID " +
                "INNER JOIN Category c ON t.categoryID = c.categoryID;", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Selects the all the transactions from database.
     *
     * @param transName   the Transaction Name.
     * @param accountName the account name.
     */
    public Cursor selectTrans(String transName, String accountName) {
        Cursor c = database.rawQuery("SELECT " +
                "t.transactionID AS transactionID, " +
                "t.transType AS transType, " +
                "t.description AS description, " +
                "t.transactionValue AS transactionValue, " +
                "t.creationDate AS creationDate, " +
                "a.accountName AS accountName, " +
                "c.categoryName AS categoryName " +
                "FROM Transactions t " +
                "INNER JOIN Account a ON t.accountID = a.accountID " +
                "INNER JOIN Category c ON t.categoryID = c.categoryID " +
                "WHERE t.description LIKE '" + transName + "' " +
                "AND a.accountName LIKE '" + accountName + "';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Selects the Sum value of all transactions from one account.
     *
     * @param accountName the account name.
     * @return a Cursor object containing the data brought from the query.
     */
    public Cursor getTransValueData(String accountName) {
        Cursor c = database.rawQuery("SELECT SUM(transactionValue) AS totalBalance FROM Transactions WHERE accountID = " +
                "(SELECT accountID FROM Account WHERE accountName LIKE '" + accountName + "');", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Selects the Sum value of all transactions from one account.
     *
     * @param accountName the account name.
     * @return a Cursor object containing the data brought from the query.
     */
    public Cursor readAccountTrans(String accountName) {
        Cursor c = database.rawQuery("SELECT transactionID AS _id, transType, description, transactionValue " +
                "FROM Transactions WHERE accountID = " +
                "(SELECT accountID FROM Account WHERE accountName LIKE '" + accountName + "');", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * Reads only the description and the value of all the transactions inside the Transactions table.
     * Equivalent to: SELECT description, value FROM Transactions;
     *
     * @return a Cursor object containing the data brought from the query.
     */
    public Cursor readDataTransList() {
        Cursor c = database.rawQuery("SELECT transactionID AS _id, transType, description, transactionValue " +
                "FROM transactions ORDER BY _id DESC LIMIT 3", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    //////////////////
    // INSERT query //
    //////////////////

    /**
     * Inserts all values of a Transaction object into the Transactions table. Equivalent to
     * INSERT INTO Transactions VALUES(type, description, value, date, categoryID, accountID);
     *
     * @param type        the transaction type (expense or income).
     * @param description the transaction's description.
     * @param value       the transaction value.
     * @param date        the transaction date (not necessarily today's date).
     * @param categoryID  the ID of the category associated with the transaction.
     * @param accountID   the ID of the account that has the transaction.
     */
    public void insertData(int type, String description, Double value, String date, int categoryID, int accountID) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_TRANS_TYPE, type);
        cv.put(DBHelper.KEY_TRANS_DESCRIPTION, description);
        cv.put(DBHelper.KEY_TRANS_VALUE, value);
        cv.put(DBHelper.KEY_TRANS_CREATION_DATE, date);
        cv.put(DBHelper.KEY_CATEGORY_ID, categoryID);
        cv.put(DBHelper.KEY_ACCOUNT_ID, accountID);
        database.insert(DBHelper.TABLE_TRANSACTION, null, cv);
    }

    //////////////////
    // DELETE query //
    //////////////////

    /**
     * Deletes a given transaction from the database.
     *
     * @param transaction the transaction marked for deletion.
     */
    public void deleteTransaction(int transaction) {
        database.delete(DatabaseHandler.TABLE_TRANSACTION, DBHelper.KEY_TRANS_ID
                + " = " + transaction, null);
    }

    //////////////////
    // UPDATE query //
    //////////////////

    /**
     * Updates a transaction based on the new information set to it.
     *
     * @param id         the transaction ID.
     * @param type       the new transaction type.
     * @param desc       the new transaction description.
     * @param value      the new transaction value.
     * @param date       the new transaction creation date.
     * @param categoryID the transaction's new category ID.
     * @param accountID  the transaction's new account ID.
     */
    public void updateData(int id, int type, String desc, Double value, String date, int categoryID, int accountID) {
        ContentValues con = new ContentValues();
        con.put(DBHelper.KEY_TRANS_ID, id);
        con.put(DBHelper.KEY_TRANS_TYPE, type);
        con.put(DBHelper.KEY_TRANS_DESCRIPTION, desc);
        con.put(DBHelper.KEY_TRANS_VALUE, value);
        con.put(DBHelper.KEY_TRANS_CREATION_DATE, date);
        con.put(DBHelper.KEY_CATEGORY_ID, categoryID);
        con.put(DBHelper.KEY_ACCOUNT_ID, accountID);
        database.update(DBHelper.TABLE_TRANSACTION, con, "transactionID ='" + id + "'", null);
    }
}