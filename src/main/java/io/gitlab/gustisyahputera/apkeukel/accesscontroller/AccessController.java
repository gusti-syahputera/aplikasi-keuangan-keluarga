package io.gitlab.gustisyahputera.apkeukel.accesscontroller;

import io.gitlab.gustisyahputera.apkeukel.entitymodel.FamilyMember;


public class AccessController {

    private FamilyMember member;
    private String password;
    private Boolean verified;

    public AccessController(FamilyMember member, String password) {
        this.member = member;
        this.password = password;
    }

    public void verify() {
        this.verified = false;
    }

    public boolean isVerified() {
        return verified;
    }
}
