package com.apkeukel;

import de.rtner.security.auth.spi.SimplePBKDF2;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;


@Table(name="member")
public class FamilyMember implements Serializable {

    public static String createTable =  // copied from definition.sql file
            "CREATE TABLE IF NOT EXISTS member (\n" +
            "       member_id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "       full_name  TEXT    NOT NULL,\n" +
            "       birth_date TEXT,  -- ISO8601 string: \"YYYY-MM-DD HH:MM:SS.SSS\"\n" +
            "       role       INTEGER DEFAULT 0, -- Enum(ORDINARY, ACCOUNTANT, CHIEF)\n" +
            "       pass_key   TEXT    DEFAULT NULL\n" +
            ");";


    //region Constructors
    //==========================================================================

    /**
     * No-arg constructor used by Norm.
     */
    @Deprecated
    public FamilyMember() {
        this.memberId = 0;
        this.fullName = "";
        this.birthDate = LocalDate.ofEpochDay(0);
        this.role = Role.ORDINARY;
        this.passKey = "";
    }

    /**
     * General family member constructor.
     */
    public FamilyMember(String fullName, LocalDate birthDate, Role role, String password) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.role = role;
        this.setPassword(password);
    }
    //endregion


    //region Comparations
    //==========================================================================

    @Override
    public boolean equals(Object comparate_) {
        if (comparate_ != null || comparate_ instanceof FamilyMember) {
            FamilyMember comparate = (FamilyMember) comparate_;
            return this.hashCode() == comparate.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.memberId, this.fullName,
                this.birthDate, this.role, this.passKey);
    }
    //endregion


    //region Properties
    //==========================================================================

    private int memberId;
    private String fullName;
    private LocalDate birthDate;
    private Role role;
    private String passKey;

    @Id
    @GeneratedValue
    @Column(name="member_id")
    public int getId() {
        return memberId;
    }

    @Column(name="full_name")
    public String getFullName() {
        return fullName;
    }

    @Transient
    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Deprecated  // see [DATENORM]
    @Column(name="birth_date")
    public String getBirthDate_() {
        return birthDate.toString();
    }

    @Transient
    public int getAge() {
        return Period.between(LocalDate.now(), this.birthDate).getYears();
    }

    @Enumerated  // to save the value as number in the database
    @Column(name="role")
    public Role getRole() {
        return role;
    }

    @Column(name="pass_key")
    public String getPassKey() {
        return passKey;
    }

    @Deprecated  // used by Norm for loading data from the database
    public void setId(int memberId) {
        this.memberId = memberId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Deprecated  // see [DATENORM]
    public void setBirthDate_(String birthDate) {
        this.birthDate = LocalDate.parse(birthDate);
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    @Transient
    public void setPassword(String password) {
        this.passKey = new SimplePBKDF2().deriveKeyFormatted(password);
    }
    //endregion

}


/*
 * Notes
 *
 * [DATENORM] Norm loads date to pojo as String instead of LocalDate.
 * One trick for this situation is to make a pair of setter and getter
 * for a dummy property `birthDate_` which attached to the date
 * column in the database table.
 */