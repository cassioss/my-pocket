package edu.illinois.dscs.mypocket.model;

import java.util.Date;

/**
 * Simple class that has all attributes of a transaction.
 *
 * @author Cassio
 * @version 1.0
 */
public class Transaction {
    private TransactionType type;
    private String description;
    private double value;
    private Date creationDate;
    private Category transactionCategory;
    private Account parentAccount;

    public Transaction(TransactionType type, String description, double value, Date creationDate,
                       Category transactionCategory, Account parentAccount) {
        this.type = type;
        this.description = description;
        this.value = value;
        this.creationDate = creationDate;
        this.transactionCategory = transactionCategory;
        this.parentAccount = parentAccount;
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

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Category getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(Category transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public Account getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(Account parentAccount) {
        this.parentAccount = parentAccount;
    }
}
