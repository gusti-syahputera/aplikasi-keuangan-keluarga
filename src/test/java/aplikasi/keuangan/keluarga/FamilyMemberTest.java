package aplikasi.keuangan.keluarga;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class FamilyMemberTest {

    private DatabaseWrapper db_wrapper;

    @Before
    public void initDatabaseWrapper() {
        String[] args = {"test_aplikasi_keuangan_keluarga.db"};
        AplikasiKeuanganKeluarga.main(args);
    }

    @Test
    public void testNewMember() {
        String full_name = "First Last";
        LocalDate birth_date = LocalDate.of(2000, 12,31);
        FamilyMember.Role role = FamilyMember.Role.CHIEF;

        FamilyMember new_member = null;
        try {
            new_member = FamilyMember.newMember(full_name, birth_date, role);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        Assert.assertTrue((new_member.getId() > 0));
        System.out.println("new_member.id: " + new_member.getId());
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
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        /* But then they realized that there were mistakes on the
         * new kid data. Therefore they want to change it and
         * started to fill the member data updating form. */
        String new_full_name = "Number Two";
        LocalDate new_birth_date = LocalDate.of(2001, 12,31);
        FamilyMember.Role new_role = FamilyMember.Role.ACCOUNTANT;
        new_member.setFullName(new_full_name);
        new_member.setBirthDate(new_birth_date);
        new_member.setRole(new_role);

        /* Then they click the "Save" button in the form. */
        try {
            new_member.pushData();
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
            /* When the pushing is failed, undo the changes */
            try {
                new_member.pullData();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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
        int member_id = 1;


        FamilyMember loaded_member = null;
        try {
            loaded_member = FamilyMember.loadMember(member_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(member_id, loaded_member.getId());
    }

}
