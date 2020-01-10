package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Transaction;
import org.junit.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class TransactionDbHelperTest {

    private static TransactionDbHelper dbHelper;

    private static List<Transaction> testList;


    //region Setups
    //==========================================================================

    @BeforeClass
    public static void databaseSetUp() {
        dbHelper = new TransactionDbHelper();
        dbHelper.setJdbcUrl("jdbc:sqlite:TransactionDbHelperTest.db");
        dbHelper.dropTable();
        dbHelper.createTable();

        /* Populate testList */
        testList = new ArrayList<>();
        testList.add(new Transaction(1, 1000, LocalDate.parse("2020-01-05"), "An income"));
        testList.add(new Transaction(1, -500, LocalDate.parse("2020-01-06"), "An expense"));
        testList.add(new Transaction(2, 700, LocalDate.parse("2020-01-05"), "Another income"));
        testList.add(new Transaction(3, 900, LocalDate.parse("2020-01-10"), "More income"));

        /* Insert transactions in testList into database */
        testList.forEach(dbHelper::insert);
    }

    @AfterClass
    public static void databaseTearDown() {
        dbHelper.close();
    }

    private void assertResultMatchs(List<Transaction> retreivedList, int... expectedIndices) {
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
        Transaction testTransaction = testList.get(0);
        int transactionId = testTransaction.getId();

        /* When */
        Query query = dbHelper.where(Transaction.whereKeyClause, transactionId);
        Transaction retreivedAccount = query.first(Transaction.class);

        /* Then */
        Assert.assertEquals(testTransaction, retreivedAccount);  // see [ASSERTEQUALS]
    }
    //endregion


    //region General tests
    //==========================================================================

    @Test
    public void whenGetTransaction_thenRetreiveFromDatabase() {

        /* Given */
        Transaction testTransaction = testList.get(0);
        int txId = testTransaction.getId();

        /* When */
        Transaction retreivedTransaction = dbHelper.getTransaction(txId);

        /* Then */
        Assert.assertEquals(testTransaction, retreivedTransaction);  // see [ASSERTEQUALS]
    }
    //endregion


    //region Search tests
    //==========================================================================

    @Test
    public void givenEmptyAccountIds_whenSearchAccount() {

        /* Given */
        int[] accountIds = {};

        /* When */
        List<Transaction> retreivedList = dbHelper.searchTransaction(accountIds, null, null, null, null, null);

        /* Then */
        assertResultMatchs(retreivedList, 0, 1, 2, 3);
    }

    @Test
    public void givenAccountIds_whenSearchAccount() {

        /* Given */
        int[] accountIds = {1, 2, 4};

        /* When */
        List<Transaction> retreivedList = dbHelper.searchTransaction(accountIds, null, null, null, null, null);

        /* Then */
        assertResultMatchs(retreivedList, 0, 1, 2);
    }

    @Test
    public void givenLowerAmount_whenSearchAccount() {

        /* Given */
        double lowerAmount = 700;

        /* When */
        List<Transaction> retreivedList = dbHelper
                .searchTransaction(null, lowerAmount, null, null, null, null);

        /* Then */
        assertResultMatchs(retreivedList, 0, 2, 3);
    }

    @Test
    public void givenUpperAmount_whenSearchAccount() {

        /* Given */
        double upperAmount = 900;

        /* When */
        List<Transaction> retreivedList = dbHelper
                .searchTransaction(null, null, upperAmount, null, null, null);

        /* Then */
        assertResultMatchs(retreivedList, 1, 2, 3);
    }

    @Test
    public void givenStartDate_whenSearchAccount() {

        /* Given */
        LocalDate startDate = LocalDate.parse("2020-01-06");

        /* When */
        List<Transaction> retreivedList = dbHelper
                .searchTransaction(null, null, null, startDate, null, null);

        /* Then */
        assertResultMatchs(retreivedList, 1, 3);
    }

    @Test
    public void givenEndDate_whenSearchAccount() {

        /* Given */
        LocalDate endDate = LocalDate.parse("2020-01-06");

        /* When */
        List<Transaction> retreivedList = dbHelper
                .searchTransaction(null, null, null, null, endDate, null);

        /* Then */
        assertResultMatchs(retreivedList, 0, 1, 2);
    }

    @Test
    public void givenDescription_whenSearchAccount() {

        /* Given */
        String description = "%income%";

        /* When */
        List<Transaction> retreivedList = dbHelper
                .searchTransaction(null, null, null, null, null, description);

        /* Then */
        assertResultMatchs(retreivedList, 0, 2, 3);
    }

    @Test
    public void givenOrderingParameters_whenSearchAccount() {

        /* Given */
        String orderBy = Transaction.transactionIdColumn;
        boolean ascendingOrder = false;

        /* When */
        List<Transaction> retreivedList = dbHelper
                .searchTransaction(null, null, null, null, null, null,
                                   orderBy, ascendingOrder);

        /* Then */
        assertResultMatchs(retreivedList, 3, 2, 1, 0);
    }

    @Test
    public void givenPaginationParameters_whenSearchAccount() {

        /* Given */
        String orderBy = Transaction.descriptionColumn;
        String offset = testList.get(0).getDescription();  // offset is exclusive
        int limit = 10;

        /* When */
        List<Transaction> retreivedList = dbHelper
                .searchTransaction(null, null, null, null, null, null,
                                   orderBy, true, offset, limit);

        /* Then */
        assertResultMatchs(retreivedList, 2, 3);
    }
    //endregion

    /*
     * Notes
     *
     * [ASSERTEQUALS] assertEquals depend on the result of TransactionTest
     * .givenSameTransactionButDifferentObjects_whenIsEquals_thenTrue()
     */
}
