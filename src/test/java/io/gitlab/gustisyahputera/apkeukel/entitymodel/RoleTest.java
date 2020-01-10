package io.gitlab.gustisyahputera.apkeukel.entitymodel;

import org.junit.Assert;
import org.junit.Test;

public class RoleTest {

    @Test
    public void givenChiefRole_whenCheckPrivileges() {

        /* Given */
        Role role = Role.CHIEF;

        /* When & Then */
        Assert.assertTrue(role.canManageFamilyMember);
        Assert.assertFalse(role.canManageAccount);
        Assert.assertFalse(role.canManageTransaction);
    }

    @Test
    public void givenAccountRole_whenCheckPrivileges() {

        /* Given */
        Role role = Role.ACCOUNTANT;

        /* When & Then */
        Assert.assertFalse(role.canManageFamilyMember);
        Assert.assertTrue(role.canManageAccount);
        Assert.assertTrue(role.canManageTransaction);
    }

    @Test
    public void givenOrdinaryRole_whenCheckPrivileges() {

        /* Given */
        Role role = Role.ORDINARY;

        /* When & Then */
        Assert.assertFalse(role.canManageFamilyMember);
        Assert.assertFalse(role.canManageAccount);
        Assert.assertFalse(role.canManageTransaction);
    }
}
