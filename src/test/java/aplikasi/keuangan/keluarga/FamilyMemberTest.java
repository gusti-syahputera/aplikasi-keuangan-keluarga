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
        /* Add new member */
        String full_name = "Second Kid";
        LocalDate birth_date = LocalDate.of(2000, 12,31);
        FamilyMember.Role role = FamilyMember.Role.ORDINARY;

        FamilyMember new_member = null;
        try {
            new_member = FamilyMember.newMember(full_name, birth_date, role);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        /* Update member data */
        String new_full_name = "The Wife";
        LocalDate new_birth_date = LocalDate.of(1990, 12,31);
        FamilyMember.Role new_role = FamilyMember.Role.ACCOUNTANT;

        try {
            new_member.setFullName(new_full_name);
            new_member.setBirthDate(new_birth_date);
            new_member.setRole(new_role);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals(new_full_name, new_member.getFullName());
        Assert.assertEquals(new_birth_date, new_member.getBirthDate());
        Assert.assertEquals(new_role, new_member.getRole());
        Assert.assertEquals(Period.between(new_birth_date, LocalDate.now()).getYears(), new_member.getAge());
    }

}
