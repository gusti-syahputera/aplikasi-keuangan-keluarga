package com.apkeukel;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;


@Table(name="transaction_")
public class Transaction implements Serializable {

    public static String createTable =  // copied from definition.sql file
            "CREATE TABLE IF NOT EXISTS transaction_ (\n" +
            "       tx_id       INTEGER PRIMARY KEY NOT NULL,\n" +
            "       account_id  INTEGER NOT NULL,\n" +
            "       amount      REAL NOT NULL,\n" +
            "       date_       TEXT NOT NULL,  -- ISO8601 string: \"YYYY-MM-DD HH:MM:SS.SSS\"\n" +
            "       description TEXT,\n" +
            "       FOREIGN KEY (account_id) REFERENCES account(account_id)\n" +
            "         ON DELETE SET NULL\n" +
            ");";


    //region Constructors
    //==========================================================================

    public Transaction() {}

    public Transaction(int accountId, double amount,
                       LocalDate date, String description) {
        this.setAccountId(accountId);
        this.setAmount(amount);
        this.setDate(date);
        this.setDescription(description);
    }

    public Transaction(Account account, double amount,
                       LocalDate date, String description) {
        this(account.getId(), amount, date, description);
    }
    //endregion


    //region Properties
    //==========================================================================

    private int accountId;
    private double amount;
    private LocalDate date;
    private String description;

    @Column(name="account_id")
    public int getAccountId() {
        return this.accountId;
    }

    @Column(name="amount")
    public double getAmount() {
        return this.amount;
    }

    @Column(name="date_")
    public LocalDate getDate() {
        return this.date;
    }

    @Column(name="description")
    public String getDescription() {
        return this.description;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAccount(Account account) {
        this.accountId = account.getId();
    }
    //endregion

}
