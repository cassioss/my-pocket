package edu.illinois.dscs.mypocket.model;

import java.util.ArrayList;

/**
 * @author Dennis
 * @version 1.0
 */
public class Account {

    private int accountID;
    private String name;
    private double initialValue;
    private boolean accountActive;
    private ArrayList<Transaction> transactionList;

    /**
     * Creates a new transaction object.
     *
     * @param name         the name of the transaction.
     * @param initialValue the name of the transaction.
     */
    public Account(String name, double initialValue, boolean accountActive) {
        this.name = name;
        this.initialValue = initialValue;
        this.accountActive = accountActive;
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

    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(double currentBalance) {
        this.initialValue = currentBalance;
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
    }
}
