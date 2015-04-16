package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.illinois.dscs.mypocket.model.Transaction;

/**
 * @author Cassio, Dennis
 * @version 1.0
 */
public class TransactionDAO {

    private SQLiteDatabase database;
    private DatabaseHandler dbHandler;
    private String[] allTransaction = {DatabaseHandler.KEY_TRANS_ID, DatabaseHandler.KEY_TRANS_TYPE,
            DatabaseHandler.KEY_DESCRIPTION, DatabaseHandler.KEY_CREATION_DATE,
            DatabaseHandler.KEY_CATEGORY_ID, DatabaseHandler.KEY_ACCOUNT_ID};

    public TransactionDAO(Context context) {
        dbHandler = new DatabaseHandler(context);
    }

    public void open() throws SQLException {
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        dbHandler.close();
    }

    public Transaction createTransaction(int type, String description, double value, String creationDate, int categoryID, int accountID) {
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

    public void deleteTransaction(Transaction transaction) {
        long id = transaction.getTransactionID();
        System.out.println("Transaction deleted with id: " + id);
        database.delete(DatabaseHandler.TABLE_TRANSACTION, DatabaseHandler.KEY_TRANS_ID
                + " = " + id, null);
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<Transaction>();

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