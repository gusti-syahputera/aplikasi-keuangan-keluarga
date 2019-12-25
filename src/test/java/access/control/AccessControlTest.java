package access.control;

import aplikasi.keuangan.keluarga.AplikasiKeuanganKeluarga;
import aplikasi.keuangan.keluarga.FamilyMember;
import aplikasi.keuangan.keluarga.FamilyMemberException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AccessControlTest {

    @Before
    public void initDatabaseWrapper() {
        String[] args = {"test_access_control.db"};
        AplikasiKeuanganKeluarga.main(args);
    }

    @Test
    public void testUserLogin() {
        /* An unauthenticated user is trying to authenticate themselves as
         * a family member. */
        int member_id = 1;
        FamilyMember member = null;
        try {
            member = new FamilyMember(member_id);
        } catch (FamilyMemberException ignored) {}
        String password = "T5B5YwyctYmhFS5J";  //derived key: "08CBC79A4722D5DB:1000:6F578C7316A2776E64E166B8283AC2F49D82BFFB"

        /* The user clicks "Login" button */
        try {
            AccessControl access_control = new AccessControl(member, password);
            AplikasiKeuanganKeluarga.assignAccessControl(access_control);
        } catch (AuthFailException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        /* The user is successfully authenticated */
        Assert.assertEquals(member, AplikasiKeuanganKeluarga.getAccessControl().getIdentity());
    }

    @Test
    public void testUserLoginFail() {
        /* An unauthenticated user is trying to authenticate themselves as
         * a family member. */
        int member_id = 1;
        FamilyMember member = null;
        try {
            member = new FamilyMember(member_id);
        } catch (FamilyMemberException ignored) {}
        String wrong_password = "T5B5YwyctYmhFS5JWRONG";  //wrong pass

        /* The user clicks "Login" button */
        try {
            AccessControl access_control = new AccessControl(member, wrong_password);
            AplikasiKeuanganKeluarga.assignAccessControl(access_control);
            Assert.fail("AuthFailException was not thrown.");
        } catch (AuthFailException expected) {}
    }
}
