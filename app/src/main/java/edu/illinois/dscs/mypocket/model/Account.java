package edu.illinois.dscs.mypocket.model;

/**
 * @author Dennis
 * @version 1.0
 */
public class Account {

    private int accountID;
    private String name;
    private double initialValue;
    private double currentBalance;
    private boolean accountActive;

    /**
     * Creates a new Transaction object.
     *
     * @param accountID     the account ID (created automatically).
     * @param name          the name of the transaction.
     * @param initialValue  the name of the transaction.
     * @param accountActive a boolean that checks if the account is active or not (if not, it is not considered for the total balance).
     */
    public Account(int accountID, String name, double initialValue, boolean accountActive) {
        this.accountID = accountID;
        this.name = name;
        this.initialValue = initialValue;
        this.currentBalance = this.initialValue;
        this.accountActive = accountActive;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(double currentBalance) {
        this.initialValue = currentBalance;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
    }
}
