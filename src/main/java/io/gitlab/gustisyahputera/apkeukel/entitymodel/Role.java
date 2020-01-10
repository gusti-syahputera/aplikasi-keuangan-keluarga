package io.gitlab.gustisyahputera.apkeukel.entitymodel;


public enum Role {

    ORDINARY(false, false, false),
    ACCOUNTANT(false, true, true),
    CHIEF(true, false, false);

    public boolean canManageFamilyMember;
    public boolean canManageAccount;
    public boolean canManageTransaction;

    Role(boolean canManageFamilyMember,
         boolean canManageAccount,
         boolean canManageTransaction) {
        this.canManageFamilyMember = canManageFamilyMember;
        this.canManageAccount = canManageAccount;
        this.canManageTransaction = canManageTransaction;
    }
}
