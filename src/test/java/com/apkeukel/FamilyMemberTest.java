package com.apkeukel;

import com.dieselpoint.norm.Database;
import org.junit.*;

import java.time.LocalDate;
import java.time.Period;


public class FamilyMemberTest {

    private static Database database;
    private FamilyMember familyMember;


    //region Setups
    //==========================================================================

    @BeforeClass
    public static void databaseSetUp() {
        database = new Database();
        database.setJdbcUrl("jdbc:sqlite:test.db");
        database.sql("DROP TABLE IF EXISTS member").execute();
        database.sql(FamilyMember.createTable).execute();
    }

    @AfterClass
    public static void databaseTearDown() {
        database.close();
    }

    @Before
    public void givenBiodata_whenCreateNewFamilyMember() {

        /* Given */
        String fullName = "Foo Bar";
        LocalDate birthDate = LocalDate.of(1999,12,31);
        Role role = Role.ORDINARY;
        String password = "Spam Egg";

        /* When */
        familyMember = new FamilyMember(fullName, birthDate, role, password);
        int memberAge = Period.between(LocalDate.now(), familyMember.getBirthDate()).getYears();

        /* Then */
        Assert.assertEquals(fullName, familyMember.getFullName());
        Assert.assertEquals(birthDate, familyMember.getBirthDate());
        Assert.assertEquals(role, familyMember.getRole());
        Assert.assertEquals(memberAge, familyMember.getAge());

        /* Verifying the key might be expensive */
//        boolean isKeyVerified = new SimplePBKDF2().verifyKeyFormatted(familyMember.getPassKey(), password);
//        Assert.assertTrue(isKeyVerified);
    }
    //endregion


    @Test
    public void givenNewBiodata_whenUpdateNewFamilyMember() {

        /* Given */
        String fullName = "Candy Bar";
        LocalDate birthDate = LocalDate.of(1999,12,30);
        Role role = Role.ACCOUNTANT;
        String password = "SpamSpamSpam Egg";

        /* When */
        familyMember.setFullName(fullName);
        familyMember.setBirthDate(birthDate);
        familyMember.setRole(role);
        familyMember.setPassword(password);

        /* Then */
        Assert.assertEquals(fullName, familyMember.getFullName());
        Assert.assertEquals(birthDate, familyMember.getBirthDate());
        Assert.assertEquals(role, familyMember.getRole());

        /* Verifying the key might be expensive */
//        boolean isKeyVerified = new SimplePBKDF2().verifyKeyFormatted(familyMember.getPassKey(), password);
//        Assert.assertTrue(isKeyVerified);
    }

    @Test
    public void givenFamilyMember_whenInsertToDatabase_thenGetGeneratedId() {

        /* Given & when */
        database.insert(familyMember);

        /* Then */
        Assert.assertNotEquals(0, familyMember.getId());
    }

    @Test
    public void givenUpdatedFamilyMember_whenUpdateToDatabase() {

        /* Given */
        String fullName = "Candy Bar";
        LocalDate birthDate = LocalDate.of(1999,12,30);
        Role role = Role.ACCOUNTANT;
        String password = "SpamSpamSpam Egg";
        familyMember.setFullName(fullName);
        familyMember.setBirthDate(birthDate);
        familyMember.setRole(role);
        familyMember.setPassword(password);
        database.insert(familyMember);

        /* When */
        database.update(familyMember);
    }

    @Test
    public void givenFamilyMember_whenDeleteInDatabase() {

        /* Given */
        database.insert(familyMember);

        /* When */
        int affectedRow = database.delete(familyMember).getRowsAffected();

        /* Then */
        Assert.assertEquals(1, affectedRow);
    }

    @Test
    public void givenMemberId_whenReadFromDatabase() {

        /* Given */
        database.insert(familyMember);
        int memberId = familyMember.getId();

        /* When */
        FamilyMember retreivedMember = database.where("member_id = ?", memberId).first(FamilyMember.class);

        /* Then */
        Assert.assertEquals(familyMember, retreivedMember);
    }

}
