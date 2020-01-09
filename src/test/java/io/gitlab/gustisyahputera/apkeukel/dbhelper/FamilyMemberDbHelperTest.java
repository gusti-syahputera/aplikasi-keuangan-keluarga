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


    //region Setups
    //==========================================================================

    @BeforeClass
    public static void databaseSetUp() {
        dbHelper = new FamilyMemberDbHelper();
        dbHelper.setJdbcUrl("jdbc:sqlite:test.db");
    }

    @AfterClass
    public static void databaseTearDown() {
        dbHelper.close();
    }

    @Before
    public void setUp() {
        dbHelper.dropTable();
        dbHelper.createTable();
    }

    private FamilyMember createSingleFamilyMember() {
        String fullName = "Foo Bar";
        LocalDate birthDate = LocalDate.of(1999, 12, 31);
        Role role = Role.ORDINARY;
        String passkey = "1DE7699A91D40069:1000:E8A86EE8A1B866E4C86D609760C385553D9F5AAF";
        return new FamilyMember(fullName, birthDate, role, passkey);
    }

    private List<FamilyMember> createFamilyMemberList() {
        String passkey = "1DE7699A91D40069:1000:E8A86EE8A1B866E4C86D609760C385553D9F5AAF";  // does not need to be unique in this case

        List<FamilyMember> newList = new ArrayList<>();
        newList.add(new FamilyMember("Foo1", LocalDate.parse("1999-12-31"), Role.CHIEF, passkey));
        newList.add(new FamilyMember("Foo2", LocalDate.parse("2000-01-31"), Role.ACCOUNTANT, passkey));
        newList.add(new FamilyMember("Foo3a", LocalDate.parse("2002-01-31"), Role.ORDINARY, passkey));
        newList.add(new FamilyMember("Foo3b", LocalDate.parse("2002-12-31"), Role.ORDINARY, passkey));

        return newList;
    }
    //endregion


    //region Persistency tests
    //==========================================================================

    @Test
    public void whenInsertToDatabase_thenGetGeneratedId() {
        FamilyMember testFamilyMember = createSingleFamilyMember();

        /* When */
        dbHelper.insert(testFamilyMember);

        /* Then */
        Assert.assertNotEquals(0, testFamilyMember.getId());
    }

    @Test
    public void whenLoadFromDatabase() {
        FamilyMember testFamilyMember = createSingleFamilyMember();

        /* Given */
        dbHelper.insert(testFamilyMember);
        int memberId = testFamilyMember.getId();

        /* When */
        Query query = dbHelper.where(FamilyMember.whereKeyClause, memberId);
        FamilyMember retreivedMember = query.first(FamilyMember.class);

        /* Then */
        /* Note that this assertion depend on the result of
         * FamilyMemberTest.givenSameFamilyMemberButDifferentObjects_whenIsEquals_thenTrue() */
        Assert.assertEquals(testFamilyMember, retreivedMember);
    }

    @Test
    public void whenDeleteInDatabase() {
        FamilyMember testFamilyMember = createSingleFamilyMember();

        /* Given */
        dbHelper.insert(testFamilyMember);

        /* When */
        int affectedRow = dbHelper.delete(testFamilyMember).getRowsAffected();

        /* Then */
        Assert.assertEquals(1, affectedRow);
    }
    //endregion


    //region General tests
    //==========================================================================

    @Test
    public void whenDropTable() {
        dbHelper.dropTable();
    }

    @Test
    public void whenCreateTable() {
        dbHelper.createTable();
    }

    @Test
    public void whenGetFamilyMember_thenRetreiveFromDatabase() {
        FamilyMember testFamilyMember = createSingleFamilyMember();

        /* Given */
        dbHelper.insert(testFamilyMember);
        int memberId = testFamilyMember.getId();

        /* When */
        FamilyMember retreivedMember = dbHelper.getFamilyMember(memberId);

        /* Then */
        /* Note that this assertion depend on the result of
         * FamilyMemberTest.givenSameFamilyMemberButDifferentObjects_whenIsEquals_thenTrue() */
        Assert.assertEquals(testFamilyMember, retreivedMember);
    }

    @Test
    public void givenName_whenSearchFamilyMember() {
        List<FamilyMember> testList = createFamilyMemberList();
        testList.forEach(dbHelper::insert);

        /* Given */
        String name = "Foo3%";

        /* When */
        List<FamilyMember> retreivedList = dbHelper.searchFamilyMember(name, null, null, null);

        /* Then */
        Assert.assertEquals(2, retreivedList.size());  // 2, 3
    }

    @Test
    public void givenStartBod_whenSearchFamilyMember() {
        List<FamilyMember> testList = createFamilyMemberList();
        testList.forEach(dbHelper::insert);

        /* Given */
        LocalDate startBod = testList.get(1).getBirthDate();

        /* When */
        List<FamilyMember> retreivedList = dbHelper.searchFamilyMember(null, startBod, null, null);

        /* Then */
        Assert.assertEquals(3, retreivedList.size());  // 1, 2, 3
    }

    @Test
    public void givenEndBod_whenSearchFamilyMember() {
        List<FamilyMember> testList = createFamilyMemberList();
        testList.forEach(dbHelper::insert);

        /* Given */
        LocalDate endBod = testList.get(2).getBirthDate();

        /* When */
        List<FamilyMember> retreivedList = dbHelper.searchFamilyMember(null, null, endBod, null);

        /* Then */
        Assert.assertEquals(3, retreivedList.size());  // 0, 1, 2
    }

    @Test
    public void givenRole_whenSearchFamilyMember() {
        List<FamilyMember> testList = createFamilyMemberList();
        testList.forEach(dbHelper::insert);

        /* Given */
        Role role = Role.ORDINARY;

        /* When */
        List<FamilyMember> retreivedList = dbHelper.searchFamilyMember(null, null, null, role);

        /* Then */
        Assert.assertEquals(2, retreivedList.size());  // 2, 3
    }
    //endregion

}
