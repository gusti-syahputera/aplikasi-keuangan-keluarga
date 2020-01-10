package io.gitlab.gustisyahputera.apkeukel.entitymodel;

import org.junit.*;

import java.time.LocalDate;
import java.time.Period;


public class FamilyMemberTest {

    private FamilyMember testFamilyMember;


    //region Setups
    //==========================================================================

    @Before
    public void setUp() {
        /* Prepare test instance. Note that this process depend on
         * the result of whenCreateWithParameterConstructor() */
        String fullName = "Foo Bar";
        LocalDate birthDate = LocalDate.of(1999, 12, 31);
        Role role = Role.ORDINARY;
        String passkey = "1DE7699A91D40069:1000:E8A86EE8A1B866E4C86D609760C385553D9F5AAF";
        testFamilyMember = new FamilyMember(fullName, birthDate, role, passkey);
    }
    //endregion


    //region General tests
    //==========================================================================

    @Test
    public void whenCreateAndSetProperties() {

        /* Given */
        String fullName = "Foo Bar";
        LocalDate birthDate = LocalDate.of(1999, 12, 31);
        Role role = Role.ORDINARY;
        String passkey = "1DE7699A91D40069:1000:E8A86EE8A1B866E4C86D609760C385553D9F5AAF";

        /* When */
        testFamilyMember = new FamilyMember();
        testFamilyMember.setFullName(fullName);
        testFamilyMember.setBirthDate(birthDate);
        testFamilyMember.setRole(role);
        testFamilyMember.setPassKey(passkey);
        int memberAge = Period.between(LocalDate.now(), testFamilyMember.getBirthDate()).getYears();

        /* Then */
        Assert.assertEquals(fullName, testFamilyMember.getFullName());
        Assert.assertEquals(birthDate, testFamilyMember.getBirthDate());
        Assert.assertEquals(role, testFamilyMember.getRole());
        Assert.assertEquals(passkey, testFamilyMember.getPassKey());
        Assert.assertEquals(memberAge, testFamilyMember.getAge());
    }

    @Test
    public void whenCreateWithParameterConstructor() {

        /* Given */
        String fullName = "Foo Bar";
        LocalDate birthDate = LocalDate.of(1999, 12, 31);
        Role role = Role.ORDINARY;
        String passkey = "1DE7699A91D40069:1000:E8A86EE8A1B866E4C86D609760C385553D9F5AAF";

        /* When */
        FamilyMember testFamilyMember = new FamilyMember(fullName, birthDate, role, passkey);

        /* Then */
        Assert.assertEquals(fullName, testFamilyMember.getFullName());
        Assert.assertEquals(birthDate, testFamilyMember.getBirthDate());
        Assert.assertEquals(role, testFamilyMember.getRole());
        Assert.assertEquals(passkey, testFamilyMember.getPassKey());
    }
    //endregion


    //region Comparation tests
    //==========================================================================
    @Test
    public void givenSelf_whenIsEquals_thenTrue() {
        Assert.assertEquals(testFamilyMember, testFamilyMember);
    }

    @Test
    public void givenSameFamilyMemberButDifferentObjects_whenIsEquals_thenTrue() {
        String fullName = "Foo Bar";
        LocalDate birthDate = LocalDate.of(1999, 12, 31);
        Role role = Role.ORDINARY;
        String passkey = "1DE7699A91D40069:1000:E8A86EE8A1B866E4C86D609760C385553D9F5AAF";

        /* Given */
        FamilyMember testFamilyMember1 = new FamilyMember(fullName, birthDate, role, passkey);
        FamilyMember testFamilyMember2 = new FamilyMember(fullName, birthDate, role, passkey);

        /* Then */
        Assert.assertTrue(testFamilyMember1.equals(testFamilyMember2));  // see [ASSERTEQUALS]
        Assert.assertTrue(testFamilyMember2.equals(testFamilyMember1));  // see [ASSERTEQUALS]
    }

    @Test
    public void givenNull_whenIsEquals_thenFalse() {
        Assert.assertFalse(testFamilyMember.equals(null));
    }

    @Test
    public void givenStrangeObject_whenIsEquals_thenFalse() {
        Assert.assertFalse(testFamilyMember.equals(""));
    }

    @Test
    public void givenOutdatedFamilyMember_whenIsEquals_thenFalse() {
        String fullName1 = "Foo Bar";
        String fullName2 = "Spam Egg";
        LocalDate birthDate = LocalDate.of(1999, 12, 31);
        Role role = Role.ORDINARY;
        String passkey = "1DE7699A91D40069:1000:E8A86EE8A1B866E4C86D609760C385553D9F5AAF";

        /* Given an instance with same ID but has different data */
        FamilyMember testFamilyMember1 = new FamilyMember(fullName1, birthDate, role, passkey);
        testFamilyMember1.setId(1);
        FamilyMember testFamilyMember2 = new FamilyMember(fullName2, birthDate, role, passkey);
        testFamilyMember2.setId(1);

        /* Then */
        Assert.assertFalse(testFamilyMember1.equals(testFamilyMember2));
        Assert.assertFalse(testFamilyMember2.equals(testFamilyMember1));
    }

    /* Notes
     *
     * [ASSERTEQUALS] Manual invocation of the equals() methods
     * is used as the assertEquals() and assertNotEquals() from
     * Assert do not invoke the equals() which is the object of
     * test.
     */
    //endregion

}
