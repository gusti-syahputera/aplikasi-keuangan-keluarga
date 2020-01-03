package com.apkeukel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Table(name="account")
public class Account implements Serializable {

    public final static String createTableQuery =  // copied from definition.sql file
            "CREATE TABLE IF NOT EXISTS account (\n" +
            "       account_id   INTEGER PRIMARY KEY,\n" +
            "       account_name TEXT    NOT NULL,\n" +
            "       owner_id     INTEGER NOT NULL,\n" +
            "       FOREIGN KEY (owner_id) REFERENCES member(member_id)\n" +
            "         ON DELETE SET NULL\n" +
            ");";
    public final static String dropTableQuery = "DROP TABLE IF EXISTS account;";
    public final static String tableName = "account";
    public final static String whereKeyClause = "account_id=?";

    //region Constructors
    //==========================================================================

    public Account() {}

    public Account(String accountName, int ownerId) {
        this.setAccountName(accountName);
        this.setOwnerId(ownerId);
    }

    public Account(String accountName, FamilyMember owner) {
        this(accountName, owner.getId());
    }
    //endregion


    //region Properties
    //==========================================================================

    private int accountId;
    private String accountName;
    private int ownerId;

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
        return Objects.hash(this.accountId, this.accountName, this.ownerId);
    }
    //endregion

}
