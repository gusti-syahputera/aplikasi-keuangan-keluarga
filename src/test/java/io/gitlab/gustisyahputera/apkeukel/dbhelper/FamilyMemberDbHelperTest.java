package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.FamilyMember;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Role;
import org.junit.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class FamilyMemberDbHelperTest {

    private static FamilyMemberDbHelper dbHelper;

    private static List<FamilyMember> testList;


    //region Setups
    //==========================================================================

    @BeforeClass
    public static void databaseSetUp() {
        dbHelper = new FamilyMemberDbHelper();
        dbHelper.setJdbcUrl("jdbc:sqlite:FamilyMemberDbHelperTest.db");
        dbHelper.dropTable();
        dbHelper.createTable();

        /* Populate testList */
        String passkey = "1DE7699A91D40069:1000:E8A86EE8A1B866E4C86D609760C385553D9F5AAF";  // does not need to be unique in this case
        testList = new ArrayList<>();
        testList.add(new FamilyMember("Foo1", LocalDate.parse("1999-12-31"), Role.CHIEF, passkey));
        testList.add(new FamilyMember("Foo2", LocalDate.parse("2000-01-31"), Role.ACCOUNTANT, passkey));
        testList.add(new FamilyMember("Foo3a", LocalDate.parse("2002-01-31"), Role.ORDINARY, passkey));
        testList.add(new FamilyMember("Foo3b", LocalDate.parse("2002-12-31"), Role.ORDINARY, passkey));

        /* Insert family members in testList into database */
        testList.forEach(dbHelper::insert);
    }

    @AfterClass
    public static void databaseTearDown() {
        dbHelper.close();
    }

    /** Assert that retreivedList contains all expected testList's elements */
    private void assertResultMatchs(List<FamilyMember> retreivedList, int... expectedIndices) {
        Assert.assertEquals(expectedIndices.length, retreivedList.size());
        for (int i = 0; i < expectedIndices.length; i++) {
            Assert.assertEquals(testList.get(expectedIndices[i]), retreivedList.get(i));  // see [ASSERTEQUALS]
        }
    }
    //endregion


    //region Persistency tests
    //==========================================================================

    @Test
    public void whenLoadFromDatabase() {

        /* Given */
        FamilyMember testFamilyMember = testList.get(0);
        int memberId = testFamilyMember.getId();

        /* When */
        Query query = dbHelper.where(FamilyMember.whereKeyClause, memberId);
        FamilyMember retreivedMember = query.first(FamilyMember.class);

        /* Then */
        Assert.assertEquals(testFamilyMember, retreivedMember);  // see [ASSERTEQUALS]
    }
    //endregion


    //region General tests
    //==========================================================================

    @Test
    public void whenGetFamilyMember_thenRetreiveFromDatabase() {

        /* Given */
        FamilyMember testFamilyMember = testList.get(0);
        int memberId = testFamilyMember.getId();

        /* When */
        FamilyMember retreivedMember = dbHelper.getFamilyMember(memberId);

        /* Then */
        Assert.assertEquals(testFamilyMember, retreivedMember);  // see [ASSERTEQUALS]
    }
    //endregion


    //region Search tests
    //==========================================================================

    @Test
    public void givenName_whenSearchFamilyMember() {

        /* Given */
        String name = "Foo3%";

        /* When */
        List<FamilyMember> retreivedList = dbHelper.searchFamilyMember(name, null, null, null);

        /* Then */
        assertResultMatchs(retreivedList, 2, 3);
    }

    @Test
    public void givenStartBod_whenSearchFamilyMember() {

        /* Given */
        LocalDate startBod = testList.get(1).getBirthDate();

        /* When */
        List<FamilyMember> retreivedList = dbHelper.searchFamilyMember(null, startBod, null, null);

        /* Then */
        assertResultMatchs(retreivedList, 1, 2, 3);
    }

    @Test
    public void givenEndBod_whenSearchFamilyMember() {

        /* Given */
        LocalDate endBod = testList.get(2).getBirthDate();

        /* When */
        List<FamilyMember> retreivedList = dbHelper.searchFamilyMember(null, null, endBod, null);

        /* Then */
        assertResultMatchs(retreivedList, 0, 1, 2);
    }

    @Test
    public void givenRole_whenSearchFamilyMember() {

        /* Given */
        Role role = Role.ORDINARY;

        /* When */
        List<FamilyMember> retreivedList = dbHelper.searchFamilyMember(null, null, null, role);

        /* Then */
        assertResultMatchs(retreivedList, 2, 3);
    }
    //endregion

    /*
     * Notes
     *
     * [ASSERTEQUALS] assertEquals depend on the result of FamilyMemberTest
     * .givenSameFamilyMemberButDifferentObjects_whenIsEquals_thenTrue()
     */
}
