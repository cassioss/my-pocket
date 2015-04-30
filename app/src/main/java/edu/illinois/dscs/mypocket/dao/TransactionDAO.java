package edu.illinois.dscs.mypocket.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.dscs.mypocket.model.Transaction;

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

    private String[] allTransactions = {DBHelper.KEY_TRANS_ID,
            DBHelper.KEY_TRANS_TYPE,
            DBHelper.KEY_TRANS_DESCRIPTION,
            DBHelper.KEY_TRANS_CREATION_DATE,
            DBHelper.KEY_CATEGORY_ID,
            DBHelper.KEY_ACCOUNT_ID};

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

    /**
     * Closes the database.
     */
    public void close() {
        dbHandler.close();
    }

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

    public Cursor completeTransData() {
        Cursor c = database.rawQuery("select t.transactionID as transactionID, t.transType transType, t.description description, t.transactionValue transactionValue, t.creationDate creationDate, a.accountName accountName, c.categoryName categoryName from Transactions t inner join account a on t.accountID = a.accountID inner join category c on t.categoryID = c.categoryID;", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    public Cursor selectTrans(String transName, String accountName) {
        Cursor c = database.rawQuery("select t.transactionID as transactionID, t.transType transType, t.description description, t.transactionValue transactionValue, t.creationDate creationDate, a.accountName accountName, c.categoryName categoryName from Transactions t inner join account a on t.accountID = a.accountID inner join category c on t.categoryID = c.categoryID where t.description like '" + transName + "' and a.accountName like '"+ accountName +"';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    public Cursor getTransValueData(String accountName) {
        Cursor c = database.rawQuery("select SUM(transactionValue) AS totalBalance from Transactions where accountID = (select accountID from Account where accountName like'" + accountName + "');", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    public Cursor readAccountTrans(String accountName) {
        Cursor c = database.rawQuery("select transactionID AS _id, transType, description, transactionValue from Transactions where accountID = (select accountID from Account where accountName like'" + accountName + "');", null);
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
        Cursor c = database.rawQuery("select transactionID _id, transType, description, transactionValue from transactions order by _id desc limit 3", null);
        if (c != null) c.moveToFirst();
        return c;

        /*
        String[] someColumns = new String[]{DBHelper.KEY_TRANS_DESCRIPTION, DBHelper.KEY_TRANS_VALUE};
        Cursor c = database.query(DBHelper.TABLE_TRANSACTION, someColumns, null, null, null, null, null);
        if (c != null) c.moveToFirst();
        return c;
        */
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
                allTransactions, DatabaseHandler.KEY_TRANS_ID + " = " + insertId, null,
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
                allTransactions, null, null, null, null, null);

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

    public void updateData(int id, int type, String desc, Double value, String date, int categoryID, int accountID) {
        ContentValues con = new ContentValues();

        con.put(DBHelper.KEY_TRANS_ID, id);
        con.put(DBHelper.KEY_TRANS_TYPE, type);
        con.put(DBHelper.KEY_TRANS_DESCRIPTION, desc);
        con.put(DBHelper.KEY_TRANS_VALUE, value);
        con.put(DBHelper.KEY_TRANS_CREATION_DATE, date);
        con.put(DBHelper.KEY_CATEGORY_ID, categoryID);
        con.put(DBHelper.KEY_ACCOUNT_ID,accountID);

        database.update(DBHelper.TABLE_TRANSACTION, con, "transactionID ='" + id + "'",null);
    }
}