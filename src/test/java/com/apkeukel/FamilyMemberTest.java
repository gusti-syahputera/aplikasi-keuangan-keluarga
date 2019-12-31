package com.apkeukel;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Query;
import de.rtner.security.auth.spi.SimplePBKDF2;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.Period;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class FamilyMemberTest {

    @Mock
    private SimplePBKDF2 pbkdf2;

    @InjectMocks
    private FamilyMember familyMember = new FamilyMember();

    @InjectMocks
    private FamilyMember blankFamilyMember = new FamilyMember();

    private static Database database;


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
    public void setUp() {

        /* Set up mocks */
        when(pbkdf2.deriveKeyFormatted(anyString())).thenReturn("");

        /* Given */
        String fullName = "Foo Bar";
        LocalDate birthDate = LocalDate.of(1999, 12, 31);
        Role role = Role.ORDINARY;
        String password = "Spam Egg";

        /* When */
        familyMember.setFullName(fullName);
        familyMember.setBirthDate(birthDate);
        familyMember.setRole(role);
        familyMember.setPassword(password);
        int memberAge = Period.between(LocalDate.now(), familyMember.getBirthDate()).getYears();

        /* Then */
        Assert.assertEquals(fullName, familyMember.getFullName());
        Assert.assertEquals(birthDate, familyMember.getBirthDate());
        Assert.assertEquals(role, familyMember.getRole());
        Assert.assertEquals(memberAge, familyMember.getAge());
        verify(pbkdf2).deriveKeyFormatted(anyString());
    }
    //endregion


    //region Comparations
    //==========================================================================
    @Test
    public void givenSelf_whenIsEquals_thenTrue() {
        Assert.assertEquals(familyMember, familyMember);
    }

    @Test
    public void givenNull_whenIsEquals_thenFalse() {
        /* assertFalse is used instead assertNotEquals to make
         * sure that the equals method of the FamilyMember instance
         * is invoked (not the comparate's). */
        Assert.assertFalse(familyMember.equals(null));
    }

    @Test
    public void givenStrangeObject_whenIsEquals_thenFalse() {
        /* assertFalse is used instead assertNotEquals to make
         * sure that the equals method of the FamilyMember instance
         * is invoked (not the comparate's). */
        Assert.assertFalse(familyMember.equals(""));
    }

    @Test
    public void givenOutdatedFamilyMember_whenIsEquals_thenFalse() {

        /* Given an instance with same ID but has different data */
        blankFamilyMember.setId(familyMember.getId());

        /* assertFalse is used instead assertNotEquals to make
         * sure that the equals method of the FamilyMember instance
         * is is invoked (not the comparate's). */
        Assert.assertFalse(blankFamilyMember.equals(familyMember));
    }
    //endregion


    @Test
    public void givenCreatedFamilyMember_whenSetBiodata() {

        /* Given blankFamilyMember, which is already initialized and
         * injected but have not get its properties populated. */

        /* When */
        String fullName = "Foo Bar";
        LocalDate birthDate = LocalDate.of(1999, 12, 31);
        Role role = Role.ORDINARY;
        String password = "Spam Egg2";

        blankFamilyMember.setBiodata(fullName, birthDate, role, password);
        int memberAge = Period.between(LocalDate.now(), blankFamilyMember.getBirthDate()).getYears();

        /* Then */
        Assert.assertEquals(fullName, blankFamilyMember.getFullName());
        Assert.assertEquals(birthDate, blankFamilyMember.getBirthDate());
        Assert.assertEquals(role, blankFamilyMember.getRole());
        Assert.assertEquals(memberAge, blankFamilyMember.getAge());
        verify(pbkdf2, times(2))  // note that the mock is also used by setUp's familyMember
                .deriveKeyFormatted(anyString());         // therefore 2 invocations is expected
    }

    @Test
    public void whenInsertToDatabase_thenGetGeneratedId() {

        /* When */
        database.insert(familyMember);

        /* Then */
        Assert.assertNotEquals(0, familyMember.getId());
    }

    @Test
    public void givenUpdatedFamilyMember_whenUpdateToDatabase() {

        /* Given */
        database.insert(familyMember);

        String fullName = "Candy Bar";
        LocalDate birthDate = LocalDate.of(1999,12,30);
        Role role = Role.ACCOUNTANT;
        String password = "SpamSpamSpam Egg";

        familyMember.setFullName(fullName);
        familyMember.setBirthDate(birthDate);
        familyMember.setRole(role);
        familyMember.setPassword(password);

        /* When */
        database.update(familyMember);
    }

    @Test
    public void whenDeleteFromDatabase() {

        /* Given */
        database.insert(familyMember);

        /* When */
        int affectedRow = database.delete(familyMember).getRowsAffected();

        /* Then */
        Assert.assertEquals(1, affectedRow);
    }

    @Test
    public void givenMemberId_whenLoadFromDatabase_thenRetreiveSameMember() {

        /* Given */
        database.insert(familyMember);
        int memberId = familyMember.getId();

        /* When */
        Query query = database.where("member_id=?", memberId);
        FamilyMember retreivedMember = query.first(FamilyMember.class);

        /* Then */
        Assert.assertEquals(familyMember, retreivedMember);
    }

}
