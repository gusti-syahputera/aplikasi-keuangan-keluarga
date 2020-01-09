package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Account;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;


public class AccountDbHelperTest {

    private static AccountDbHelper dbHelper;

    private static List<Account> testList;


    //region Setups
    //==========================================================================

    @BeforeClass
    public static void databaseSetUp() {
        dbHelper = new AccountDbHelper();
        dbHelper.setJdbcUrl("jdbc:sqlite:AccountDbHelperTest.db");
        dbHelper.dropTable();
        dbHelper.createTable();

        /* Populate testList */
        testList = new ArrayList<>();
        testList.add(new Account("Family income", 1));
        testList.add(new Account("Family saving", 2));
        testList.add(new Account("School tuition", 3));
        testList.add(new Account("School tuition", 4));

        /* Insert accounts in testList into database */
        testList.forEach(dbHelper::insert);
    }

    @AfterClass
    public static void databaseTearDown() {
        dbHelper.close();
    }

    /** Assert that retreivedList contains all expected testList's elements */
    private void assertResultMatchs(List<Account> retreivedList, int... expectedIndices) {
        Assert.assertEquals(expectedIndices.length, retreivedList.size());
        for (int i = 0; i < expectedIndices.length; i++) {
            Assert.assertEquals(testList.get(expectedIndices[i]), retreivedList.get(i));  // see [ASSERTEQUALS]
        }
    }
    //endregion


    //region Persistency test
    //==========================================================================

    @Test
    public void whenLoadFromDatabase() {

        /* Given */
        Account testAccount = testList.get(0);
        int accountId = testAccount.getId();

        /* When */
        Query query = dbHelper.where(Account.whereKeyClause, accountId);
        Account retreivedAccount = query.first(Account.class);

        /* Then */
        Assert.assertEquals(testAccount, retreivedAccount);  // see [ASSERTEQUALS]
    }
    //endregion


    //region General tests
    //==========================================================================

    @Test
    public void whenGetAccount_thenRetreiveFromDatabase() {

        /* Given */
        Account testAccount = testList.get(0);
        int accountId = testAccount.getId();

        /* When */
        Account retreivedAccount = dbHelper.getAccount(accountId);

        /* Then */
        Assert.assertEquals(testAccount, retreivedAccount);  // see [ASSERTEQUALS]
    }
    //endregion


    //region Search tests
    //==========================================================================

    @Test
    public void givenAccountName_whenSearchAccount() {

        /* Given */
        String name = "%school%";

        /* When */
        List<Account> retreivedList = dbHelper.searchAccount(name, null);

        /* Then */
        assertResultMatchs(retreivedList, 2, 3);
    }

    @Test
    public void givenEmptyOwnerIds_whenSearchAccount() {

        /* Given */
        int[] ownerIds = {};

        /* When */
        List<Account> retreivedList = dbHelper.searchAccount("Family%", ownerIds);

        /* Then */
        assertResultMatchs(retreivedList, 0, 1);
    }

    @Test
    public void givenOwnerIds_whenSearchAccount() {

        /* Given */
        int[] ownerIds = {1, 4, 5};

        /* When */
        List<Account> retreivedList = dbHelper.searchAccount(null, ownerIds);

        /* Then */
        assertResultMatchs(retreivedList, 0, 3);
    }
    //endregion

    /*
     * Notes
     *
     * [ASSERTEQUALS] assertEquals depend on the result of AccountTest
     * .givenSameAccountButDifferentObjects_whenIsEquals_thenTrue()
     */
}
