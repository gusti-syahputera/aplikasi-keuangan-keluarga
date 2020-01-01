package com.apkeukel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Table(name="account")
public class Account implements Serializable {

    public static String createTable =  // copied from definition.sql file
            "CREATE TABLE IF NOT EXISTS account (\n" +
            "       account_id   INTEGER PRIMARY KEY NOT NULL,\n" +
            "       account_name TEXT    NOT NULL,\n" +
            "       owner_id     INTEGER NOT NULL,\n" +
            "       balance      INTEGER NOT NULL,\n" +
            "       FOREIGN KEY (owner_id) REFERENCES member(member_id)\n" +
            "         ON DELETE SET NULL\n" +
            ");";


    //region Constructor
    //==========================================================================

    public Account() {}

    public Account(String accountName, int ownerId, int balance) {
        this.setAccountName(accountName);
        this.setOwnerId(ownerId);
        this.setBalance(balance);
    }

    public Account(String accountName, FamilyMember owner, int balance) {
        this(accountName, owner.getId(), balance);
    }
    //endregion


    //region Properties
    //==========================================================================

    private int accountId;
    private String accountName;
    private int ownerId;
    private int balance;

    @Id
    @GeneratedValue
    @Column(name="account_id")
    public int getId() {
        return this.accountId;
    }

    @Column(name="account_name")
    public String getAccountName() {
        return this.accountName;
    }

    @Column(name="owner_id")
    public int getOwnerId() {
        return ownerId;
    }

    @Column(name="balance")
    public int getBalance() {
        return this.balance;
    }

    public void setId(int accountId) {
        this.accountId = accountId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setOwnerId(int memberId) {
        this.ownerId = memberId;
    }

    public void setOwner(FamilyMember owner) {
        this.ownerId = owner.getId();
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
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
        Account comparate = (Account) comparate_;
        return this.hashCode() == comparate.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.accountId, this.accountName,
                this.ownerId, this.balance
        );
    }
    //endregion

}
