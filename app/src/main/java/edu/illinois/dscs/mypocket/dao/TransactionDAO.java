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

    /**
     * select the all the transactions from database.
     */
    public Cursor completeTransData() {
        Cursor c = database.rawQuery("select t.transactionID as transactionID, t.transType transType, t.description description, t.transactionValue transactionValue, t.creationDate creationDate, a.accountName accountName, c.categoryName categoryName from Transactions t inner join account a on t.accountID = a.accountID inner join category c on t.categoryID = c.categoryID;", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * select the all the transactions from database.
     * @param transName   the Transaction Name.
     * @param accountName the account name.
     */
    public Cursor selectTrans(String transName, String accountName) {
        Cursor c = database.rawQuery("select t.transactionID as transactionID, t.transType transType, t.description description, t.transactionValue transactionValue, t.creationDate creationDate, a.accountName accountName, c.categoryName categoryName from Transactions t inner join account a on t.accountID = a.accountID inner join category c on t.categoryID = c.categoryID where t.description like '" + transName + "' and a.accountName like '" + accountName + "';", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * select the Sum value of all transactions from one account.
     * @param accountName the account name.
     *
     * @return a Cursor object containing the data brought from the query.
     */
    public Cursor getTransValueData(String accountName) {
        Cursor c = database.rawQuery("select SUM(transactionValue) AS totalBalance from Transactions where accountID = (select accountID from Account where accountName like'" + accountName + "');", null);
        if (c != null) c.moveToFirst();
        return c;
    }

    /**
     * select the Sum value of all transactions from one account.
     * @param accountName the account name.
     *
     * @return a Cursor object containing the data brought from the query.
     */
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
    }

    /**
     * Deletes a given transaction from the database.
     *
     * @param transaction the transaction marked for deletion.
     */
    public void deleteTransaction(int transaction) {
        //System.out.println("Transaction deleted with id: " + id);
        database.delete(DatabaseHandler.TABLE_TRANSACTION, DBHelper.KEY_TRANS_ID
                + " = " + transaction, null);
    }

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