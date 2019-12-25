package aplikasi.keuangan.keluarga;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class FamilyMemberTest {

    @Before
    public void initDbHandler() {
        String[] args = {"test_aplikasi_keuangan_keluarga.db"};
        AplikasiKeuanganKeluarga.main(args);
    }

    @Test
    public void testNewMember() {
        /* The family chief want to add a new family member. */

        /* They fill the new member form */
        String full_name = "First Last";
        LocalDate birth_date = LocalDate.of(2000, 12,31);
        FamilyMember.Role role = FamilyMember.Role.CHIEF;

        /* Then click the "Add" button */
        FamilyMember new_member = null;
        try {
            new_member = FamilyMember.newMember(full_name, birth_date, role);
        } catch (FamilyMemberException e) {
            e.getCause().printStackTrace();
            Assert.fail(e.getMessage());
        }

        /* The new member successfully added to the database */
        Assert.assertTrue((new_member.getId() > 0));
        Assert.assertEquals(full_name, new_member.getFullName());
        Assert.assertEquals(birth_date, new_member.getBirthDate());
        Assert.assertEquals(Period.between(birth_date, LocalDate.now()).getYears(), new_member.getAge());
    }

    @Test
    public void testUpdateMember() {
        /* The family chief add a new family member. */
        String full_name = "Second Kid";
        LocalDate birth_date = LocalDate.of(2000, 12,31);
        FamilyMember.Role role = FamilyMember.Role.ACCOUNTANT;
        FamilyMember new_member = null;
        try {
            new_member = FamilyMember.newMember(full_name, birth_date, role);
        } catch (FamilyMemberException e) {
            e.getCause().printStackTrace();
            Assert.fail(e.getMessage());
        }

        /* But then they realized that there were mistakes on the
         * new kid data. Therefore they want to change it and
         * started to fill the member data updating form. */
        String new_full_name = "Number Two";
        LocalDate new_birth_date = LocalDate.of(2001, 12,31);
        FamilyMember.Role new_role = FamilyMember.Role.ORDINARY;
        new_member.setFullName(new_full_name);
        new_member.setBirthDate(new_birth_date);
        new_member.setRole(new_role);

        /* Then they click the "Save" button in the form. */
        try {
            new_member.pushData();
        } catch (SQLException e) {
            /* Pushing was failed, hence undo the changes.
             * NOTE: This behaviour have not tested yet. */
            try {
                new_member.pullData();
            } catch (FamilyMemberException ex) {
                ex.getCause().printStackTrace();
            }
            e.printStackTrace();
            Assert.fail(e.getMessage());

        }

        /* Finally they are happy because the data represent their
         * kid's actual data. */
        Assert.assertEquals(new_full_name, new_member.getFullName());
        Assert.assertEquals(new_birth_date, new_member.getBirthDate());
        Assert.assertEquals(new_role, new_member.getRole());
        int new_age = Period.between(new_birth_date, LocalDate.now()).getYears();
        Assert.assertEquals(new_age, new_member.getAge());
    }

    @Test
    public void testLoadMember() {
        /* Any user want to know specific family member's data.*/

        /* Preparation */
        String full_name = "Trash Member";
        LocalDate birth_date = LocalDate.of(2000, 12,31);
        FamilyMember.Role role = FamilyMember.Role.ORDINARY;
        try {
            FamilyMember.newMember(full_name, birth_date, role);
        } catch (FamilyMemberException ignored) {}

        /* Retreive specific member data from the database */
        int member_id = 1;
        FamilyMember loaded_member = null;
        try {
            loaded_member = new FamilyMember(member_id);
        } catch (FamilyMemberException ignored) {}
        Assert.assertEquals(member_id, loaded_member.getId());
    }

    @Test
    public void testRemoveMember() {
        /* Family chief want to remove specific family member. */

        /* Preparation */
        String full_name = "Trash Member";
        LocalDate birth_date = LocalDate.of(2000, 12,31);
        FamilyMember.Role role = FamilyMember.Role.ORDINARY;
        FamilyMember new_member = null;
        try {
            new_member = FamilyMember.newMember(full_name, birth_date, role);
        } catch (FamilyMemberException ignored) {}

        /* User click the "Remove member" button */
        int member_id = new_member.getId();
        try {
            FamilyMember.removeMember(member_id);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        /* Finally the particular family member does not longer exist
         * in the database. */
        try {
            new FamilyMember(member_id);
            Assert.fail("FamilyMemberException was not thrown.");
        } catch (FamilyMemberException expected) {}
    }

}
