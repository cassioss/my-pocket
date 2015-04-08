package edu.illinois.dscs.mypocket.model;

import java.util.ArrayList;

/**
 * @author Dennis
 * @version 1.0
 */
public class Account {

    private String name;
    private double currentBalance;
    private boolean accountActive;
    private ArrayList<Transaction> transactionList;

    /**
     * Creates a new transaction object.
     *
     * @param name         the name of the transaction.
     * @param initialValue the name of the transaction.
     */
    public Account(String name, double initialValue) {
        this.name = name;
        this.currentBalance = initialValue;
        this.accountActive = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeTransaction(Transaction obj) {
        transactionList.remove(obj);
    }

    public void addTransaction(Transaction obj) {
        transactionList.add(obj);
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public void addValue(double transactionValue) {
        setCurrentBalance(this.currentBalance + transactionValue);
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
    }
}
