package edu.illinois.dscs.mypocket.model;

import java.util.ArrayList;

/**
 * Created by Dennis on 07/04/2015.
 */
public class Account {

    private String name;
    private ArrayList<Transaction> transactionList;


    /**
     * Creates a new transaction object.
     *
     * @param name the name of the transaction.
     */
    public Account(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeTransaction(Transaction obj){
         transactionList.remove(obj);
    }

    public void addTransaction(Transaction obj){
        transactionList.add(obj);
    }
}
