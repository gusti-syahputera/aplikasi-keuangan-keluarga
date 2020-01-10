package io.gitlab.gustisyahputera.apkeukel.accesscontroller;

import io.gitlab.gustisyahputera.apkeukel.entitymodel.FamilyMember;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AccessControllerTest {

    @Mock private FamilyMember mockChiefMember = new FamilyMember();
    @Mock private FamilyMember mockAccountantMember = new FamilyMember();
    @Mock private FamilyMember mockOrdinaryMember = new FamilyMember();
    private FamilyMember mockMember;

    @Mock private Pbkdf2Verifier mockVerifier = new Pbkdf2Verifier();

    private String password = "Spam Egg";
    private String passkey = "2096BC0DCE927792:1000:7902E30239A61B498829AE9D79170E0F895D21B1";
    private String wrongPassword = "Foo Bar";

    private AccessController testAccessController;


    //region Setups
    //==========================================================================

    @Before
    public void mockSetUp() {
        when(mockChiefMember.getPassKey()).thenReturn(passkey);
        when(mockAccountantMember.getPassKey()).thenReturn(passkey);
        when(mockOrdinaryMember.getPassKey()).thenReturn(passkey);

        when(mockChiefMember.getRole()).thenReturn(Role.CHIEF);
        when(mockAccountantMember.getRole()).thenReturn(Role.ACCOUNTANT);
        when(mockOrdinaryMember.getRole()).thenReturn(Role.ORDINARY);

        when(mockVerifier.deriveKey(password)).thenReturn(passkey);
        when(mockVerifier.verifyKey(passkey, password)).thenReturn(true);
        when(mockVerifier.verifyKey(passkey, wrongPassword)).thenReturn(false);

        mockMember = mockChiefMember;
    }

    private void initTestAccessController() {
        testAccessController = new AccessController(mockVerifier, mockMember, password);
    }

    private AccessController[] generateAccessControllersOfAllRole(String password) {
        return new AccessController[] {
                new AccessController(mockVerifier, mockChiefMember, password),
                new AccessController(mockVerifier, mockAccountantMember, password),
                new AccessController(mockVerifier, mockOrdinaryMember, password),
        };
    }
    //endregion


    //region Verification tests
    //==========================================================================

    @Test
    public void givenRightPassword_whenIsVerified_thenTrue() {

        /* Given */
        initTestAccessController();
        testAccessController.verify();
        verify(mockMember).getPassKey();
        verify(mockVerifier).verifyKey(passkey, password);

        /* When */
        boolean verified = testAccessController.isVerified();

        /* Then */
        Assert.assertTrue(verified);
    }

    @Test
    public void givenWrongPassword_whenIsVerified_thenFalse() {

        /* Given */
        testAccessController = new AccessController(mockVerifier, mockMember, wrongPassword);
        testAccessController.verify();
        verify(mockMember).getPassKey();
        verify(mockVerifier).verifyKey(passkey, wrongPassword);

        /* When */
        boolean verified = testAccessController.isVerified();

        /* Then */
        Assert.assertFalse(verified);
    }
    //endregion


    //region Getter tests
    //==========================================================================

    @Test
    public void whenGetIdentity() {

        /* Given */
        initTestAccessController();

        /* When */
        FamilyMember identity = testAccessController.getIdentity();

        /* Then */
        Assert.assertEquals(mockMember, identity);
    }

    @Test
    public void whenGetRole() {

        /* Given */
        initTestAccessController();

        /* When */
        Role role = testAccessController.getRole();

        /* Then */
        Assert.assertEquals(mockMember.getRole(), role);
    }
    //endregion


    //region Privileges tests
    //==========================================================================

    @Test
    public void givenVerifiedIdentity_whenCheckPrivileges() {
        AccessController[] testAccessControllers = generateAccessControllersOfAllRole(password);
        for (AccessController ac: testAccessControllers) {

            /* Given */
            ac.verify();
            Assert.assertTrue(ac.isVerified());

            /* When */
            boolean canManageFamilyMember = ac.isAuthorizedToManageFamilyMember();
            boolean canManageAccount = ac.isAuthorizedToManageAccount();
            boolean canManageTransaction = ac.isAuthorizedToManageTransaction();

            /* Then */
            Role role = ac.getRole();
            Assert.assertEquals(role.canManageFamilyMember, canManageFamilyMember);
            Assert.assertEquals(role.canManageAccount, canManageAccount);
            Assert.assertEquals(role.canManageTransaction, canManageTransaction);
        }
    }

    @Test
    public void givenUnverifiedIdentity_whenCheckPrivileges() {
        AccessController[] testAccessControllers = generateAccessControllersOfAllRole(wrongPassword);
        for (AccessController accessController: testAccessControllers) {

            /* Given */
            accessController.verify();
            Assert.assertFalse(accessController.isVerified());

            /* When */
            boolean canManageFamilyMember = accessController.isAuthorizedToManageFamilyMember();
            boolean canManageAccount = accessController.isAuthorizedToManageAccount();
            boolean canManageTransaction = accessController.isAuthorizedToManageTransaction();

            /* Then */
            Assert.assertFalse(canManageFamilyMember);
            Assert.assertFalse(canManageAccount);
            Assert.assertFalse(canManageTransaction);
        }
    }
    //endregion

}
