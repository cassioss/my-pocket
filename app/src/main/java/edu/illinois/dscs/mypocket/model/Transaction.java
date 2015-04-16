package edu.illinois.dscs.mypocket.model;

import java.util.Date;

/**
 * Simple class that has all attributes of a transaction.
 *
 * @author Cassio
 * @version 1.0
 */
public class Transaction {

    private int transactionID;
    private TransactionType type;
    private String description;
    private double value;
    private Date creationDate;
    private int categoryID;
    private int accountID;

    /**
     * Creates a transaction object.
     *
     * @param transactionID the transaction ID (generated automatically).
     * @param type          the transaction type (expense or income).
     * @param description   the transaction's description.
     * @param value         the transaction value (always positive).
     * @param creationDate  the creation date of the transaction (not necessarily today's date).
     * @param categoryID    the category ID associated with the transaction (unique).
     * @param accountID     the account that has this transaction.
     */
    public Transaction(int transactionID, TransactionType type, String description, double value, Date creationDate,
                       int categoryID, int accountID) {
        this.transactionID = transactionID;
        this.type = type;
        this.description = description;
        this.value = value;
        this.creationDate = creationDate;
        this.categoryID = categoryID;
        this.accountID = accountID;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
}