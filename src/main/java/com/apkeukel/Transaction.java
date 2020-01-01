package com.apkeukel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


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

    private int transactionId;
    private int accountId;
    private double amount;
    private LocalDate date;
    private String description;


    @Id
    @GeneratedValue
    @Column(name="tx_id")
    public int getId() {
        return this.transactionId;
    }

    @Column(name="account_id")
    public int getAccountId() {
        return this.accountId;
    }

    @Column(name="amount")
    public double getAmount() {
        return this.amount;
    }

    @Deprecated  // see [DATENORM]
    @Column(name="date_")
    public String getDate_() {
        return this.date.toString();
    }

    @Transient
    public LocalDate getDate() {
        return this.date;
    }

    @Column(name="description")
    public String getDescription() {
        return this.description;
    }

    public void setId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Deprecated  // see [DATENORM]
    public void setDate_(String date) {
        this.date = LocalDate.parse(date);
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

    /*
     * Notes
     *
     * [DATENORM] Norm loads date to pojo as String instead of LocalDate.
     * One trick for this situation is to make a pair of setter and getter
     * for a dummy property `birthDate_` which attached to the date
     * column in the database table.
     */
    //endregion


    //region Comparations
    //==========================================================================

    @Override
    public boolean equals(Object comparate_) {

        if (this == comparate_) {
            return true;
        }
        if (comparate_ == null) {
            return false;
        }
        if (this.getClass() != comparate_.getClass()) {
            return false;
        }
        Transaction comparate = (Transaction) comparate_;
        return this.hashCode() == comparate.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.transactionId, this.accountId,
                this.amount, this.date, this.description
        );
    }
    //endregion

}