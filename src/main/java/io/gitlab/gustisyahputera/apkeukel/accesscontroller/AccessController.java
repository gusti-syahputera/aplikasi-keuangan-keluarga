package io.gitlab.gustisyahputera.apkeukel.accesscontroller;

import io.gitlab.gustisyahputera.apkeukel.entitymodel.FamilyMember;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Role;


public class AccessController {

    private Verifier verifier;
    private FamilyMember identity;
    private String password;

    public AccessController(Verifier verifier,
                            FamilyMember identity,
                            String password) {
        this.verifier = verifier;
        this.identity = identity;
        this.password = password;
    }

    public FamilyMember getIdentity() {
        return this.identity;
    }

    public Role getRole() {
        return this.identity.getRole();
    }


    //region Verification
    //==========================================================================

    private Boolean verified = false;

    public void verify() {
        String passkey = this.identity.getPassKey();
        String password = this.password;
        this.verified = verifier.verifyKey(passkey, password);
    }

    public boolean isVerified() {
        return verified;
    }
    //endregion


    //region Privileges
    //==========================================================================

    public boolean isAuthorizedToManageFamilyMember() {
        return verified && this.getRole().canManageFamilyMember;
    }

    public boolean isAuthorizedToManageAccount() {
        return verified && this.getRole().canManageAccount;
    }

    public boolean isAuthorizedToManageTransaction() {
        return verified && this.getRole().canManageTransaction;
    }
    //endregion
}
