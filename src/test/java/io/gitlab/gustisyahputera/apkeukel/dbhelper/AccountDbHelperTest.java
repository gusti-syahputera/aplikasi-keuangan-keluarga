package io.gitlab.gustisyahputera.apkeukel.dbhelper;


import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Account;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;


public class AccountDbHelperTest {

    private static AccountDbHelper dbHelper;


    //region Setups
    //==========================================================================

    @BeforeClass
    public static void databaseSetUp() {
        dbHelper = new AccountDbHelper();
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

    private Account createSingleAccount() {
        return new Account("General account", 1);
    }

    private List<Account> createAccountList() {
        List<Account> newList = new ArrayList<>();
        newList.add(new Account("Family income", 1));
        newList.add(new Account("Family saving", 2));
        newList.add(new Account("School tuition", 3));
        newList.add(new Account("School tuition", 4));

        return newList;
    }
    //endregion


    //region Persistency tests
    //==========================================================================

    @Test
    public void whenInsertToDatabase_thenGetGeneratedId() {
        Account testAccount = createSingleAccount();

        /* When */
        dbHelper.insert(testAccount);

        /* Then */
        Assert.assertNotEquals(0, testAccount.getId());
    }

    @Test
    public void whenLoadFromDatabase() {
        Account testAccount = createSingleAccount();

        /* Given */
        dbHelper.insert(testAccount);
        int accountId = testAccount.getId();

        /* When */
        Query query = dbHelper.where(Account.whereKeyClause, accountId);
        Account retreivedAccount = query.first(Account.class);

        /* Then */
        /* Note that this assertion depend on the result of
         * AccountTest.givenSameAccountButDifferentObjects_whenIsEquals_thenTrue() */
        Assert.assertEquals(testAccount, retreivedAccount);
    }

    @Test
    public void whenDeleteInDatabase() {
        Account testAccount = createSingleAccount();

        /* Given */
        dbHelper.insert(testAccount);

        /* When */
        int affectedRow = dbHelper.delete(testAccount).getRowsAffected();

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
    public void whenGetAccount_thenRetreiveFromDatabase() {
        Account testAccount = createSingleAccount();

        /* Given */
        dbHelper.insert(testAccount);
        int accountId = testAccount.getId();

        /* When */
        Account retreivedAccount = dbHelper.getAccount(accountId);

        /* Then */
        /* Note that this assertion depend on the result of
         * AccountTest.givenSameAccountButDifferentObjects_whenIsEquals_thenTrue() */
        Assert.assertEquals(testAccount, retreivedAccount);
    }

    @Test
    public void givenAccountName_whenSearchAccount() {
        List<Account> testList = createAccountList();
        testList.forEach(dbHelper::insert);

        /* Given */
        String name = "%school%";

        /* When */
        List<Account> retreivedList = dbHelper.searchAccount(name, null);

        /* Then */
        Assert.assertEquals(2, retreivedList.size());  // 2, 3
        /* Note that this assertion depend on the result of
         * AccountTest.givenSameAccountButDifferentObjects_whenIsEquals_thenTrue() */
        Assert.assertEquals(retreivedList.get(0), testList.get(2));
        Assert.assertEquals(retreivedList.get(1), testList.get(3));
    }

    @Test
    public void givenEmptyOwnerIds_whenSearchAccount() {
        List<Account> testList = createAccountList();
        testList.forEach(dbHelper::insert);

        /* Given */
        int[] ownerIds = {};

        /* When */
        List<Account> retreivedList = dbHelper.searchAccount("control param", ownerIds);

        /* Then */
        Assert.assertEquals(0, retreivedList.size());
    }

    @Test
    public void givenOwnerIds_whenSearchAccount() {
        List<Account> testList = createAccountList();
        testList.forEach(dbHelper::insert);

        /* Given */
        int[] ownerIds = {1, 4, 5};

        /* When */
        List<Account> retreivedList = dbHelper.searchAccount(null, ownerIds);

        /* Then */
        Assert.assertEquals(2, retreivedList.size());  // 0, 3
        /* Note that this assertion depend on the result of
         * AccountTest.givenSameAccountButDifferentObjects_whenIsEquals_thenTrue() */
        Assert.assertEquals(retreivedList.get(0), testList.get(0));
        Assert.assertEquals(retreivedList.get(1), testList.get(3));
    }
    //endregion

}
