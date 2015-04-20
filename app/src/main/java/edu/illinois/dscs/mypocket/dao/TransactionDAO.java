package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.dscs.mypocket.model.Transaction;

/**
 * @author Cassio, Dennis
 * @version 1.0
 */
public class TransactionDAO {

    protected SQLiteDatabase database;
    protected DBHelper dbHandler;
    protected Context Context;

    private String[] allTransaction = {DatabaseHandler.KEY_TRANS_ID,
            DatabaseHandler.KEY_TRANS_TYPE,
            DatabaseHandler.KEY_DESCRIPTION,
            DatabaseHandler.KEY_CREATION_DATE,
            DatabaseHandler.KEY_CATEGORY_ID,
            DatabaseHandler.KEY_ACCOUNT_ID};

    /**
     * DAO constructor for transactions.
     *
     * @param context the database context in the phone.
     */
    public TransactionDAO(Context context) {
        Context = context;
    }


    public TransactionDAO open() {
        dbHandler = new DBHelper(Context);
        database = dbHandler.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHandler.close();
    }

    public void insertData(int type, String desc, Double value, String date, int category, int account) {
        ContentValues cv = new ContentValues();
        cv.put(dbHandler.KEY_TRANS_TYPE, type);
        cv.put(dbHandler.KEY_TRANS_DESCRIPTION, desc);
        cv.put(dbHandler.KEY_TRANS_VALUE, value);
        cv.put(dbHandler.KEY_TRANS_CREATION_DATE, date);
        cv.put(dbHandler.KEY_CATEGORY_ID, category);
        cv.put(dbHandler.KEY_ACCOUNT_ID, account);
        database.insert(dbHandler.TABLE_TRANSACTION, null, cv);
    }

    public Cursor readDataTransList() {
        String[] someColumns = new String[] { dbHandler.KEY_TRANS_DESCRIPTION,
                dbHandler.KEY_TRANS_VALUE };
        Cursor c = database.query(dbHandler.TABLE_TRANSACTION, someColumns, null,
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
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
    public Transaction createTransaction(int type, String description, double value,
                                         String creationDate, int categoryID, int accountID) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.KEY_TRANS_TYPE, type);
        values.put(DatabaseHandler.KEY_DESCRIPTION, description);
        values.put(DatabaseHandler.KEY_TRANS_VALUE, value);
        values.put(DatabaseHandler.KEY_CREATION_DATE, creationDate);
        values.put(DatabaseHandler.KEY_CATEGORY_ID, categoryID);
        values.put(DatabaseHandler.KEY_ACCOUNT_ID, accountID);
        long insertId = database.insert(DatabaseHandler.TABLE_TRANSACTION, null,
                values);
        Cursor cursor = database.query(DatabaseHandler.TABLE_TRANSACTION,
                allTransaction, DatabaseHandler.KEY_TRANS_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Transaction newTransaction = cursorToTransaction(cursor);
        cursor.close();
        return newTransaction;
    }

    /**
     * Deletes a given transaction from the database.
     *
     * @param transaction the transaction marked for deletion.
     */
    public void deleteTransaction(Transaction transaction) {
        long id = transaction.getTransactionID();
        System.out.println("Transaction deleted with id: " + id);
        database.delete(DatabaseHandler.TABLE_TRANSACTION, DatabaseHandler.KEY_TRANS_ID
                + " = " + id, null);
    }

    /**
     * Gets all the transactions from the database.
     *
     * @return a list with all items from the Transactions table turned into Transaction objects.
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHandler.TABLE_TRANSACTION,
                allTransaction, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Transaction Transaction = cursorToTransaction(cursor);
            transactions.add(Transaction);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return transactions;
    }

    /**
     * Turns a cursor item into a full transaction.
     *
     * @param cursor the cursor with a given transaction in the database.
     * @return a transaction equivalent to the one pointed by the cursor.
     */
    private Transaction cursorToTransaction(Cursor cursor) {
        Transaction transaction = new Transaction(0, 0, null, 0.0, null, 0, 0);
        transaction.setTransactionID(cursor.getInt(0));
        transaction.setType(cursor.getInt(1));
        transaction.setDescription(cursor.getString(3));
        transaction.setCreationDate(cursor.getString(4));
        transaction.setCategoryID(cursor.getInt(5));
        transaction.setAccountID(cursor.getInt(6));
        return transaction;
    }
}