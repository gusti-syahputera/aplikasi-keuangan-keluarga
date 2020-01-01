package com.apkeukel;

import com.dieselpoint.norm.Database;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionTest {

    @Mock private Account mockAccount = new Account();

    private static Database database;
    private Transaction testTransaction;


    //region Setups
    //==========================================================================

    @BeforeClass
    public static void databaseSetUp() {
        database = new Database();
        database.setJdbcUrl("jdbc:sqlite:test.db");
        database.sql("DROP TABLE IF EXISTS transaction_").execute();
        database.sql(Transaction.createTable).execute();
    }

    @AfterClass
    public static void databaseTearDown() {
        database.close();
    }

    @Before
    public void whenCreateAndSetProperties() {
        when(mockAccount.getId()).thenReturn(1);

        int accountId = mockAccount.getId();
        double amount = 500.0;
        LocalDate date = LocalDate.now();
        String description = "Initial income";

        /* When */
        testTransaction = new Transaction();
        testTransaction.setAccountId(accountId);
        testTransaction.setAmount(amount);
        testTransaction.setDate(date);
        testTransaction.setDescription(description);

        /* Then */
        Assert.assertEquals(accountId, testTransaction.getAccountId());
        Assert.assertEquals(amount, testTransaction.getAmount(), 0.0);
        Assert.assertEquals(date, testTransaction.getDate());
        Assert.assertEquals(description, testTransaction.getDescription());
    }
    //endregion


    @Test
    public void whenCreateWithParameterConstructor() {
        when(mockAccount.getId()).thenReturn(1);

        /* Given */
        int accountId = mockAccount.getId();
        double amount = 500.0;
        LocalDate date = LocalDate.now();
        String description = "Initial income";

        /* When */
        Transaction testTransaction1 = new Transaction(accountId, amount, date, description);
        Transaction testTransaction2 = new Transaction(mockAccount, amount, date, description);
        Transaction[] testTransactions = {testTransaction1, testTransaction2};

        /* Then */
        for (Transaction testTransaction: testTransactions) {
            Assert.assertEquals(accountId, testTransaction.getAccountId());
            Assert.assertEquals(amount, testTransaction.getAmount(), 0.0);
            Assert.assertEquals(date, testTransaction.getDate());
            Assert.assertEquals(description, testTransaction.getDescription());
        }
    }

    @Test
    public void whenSetAccount() {

        int accountId = mockAccount.getId();

        /* When */
        testTransaction.setAccount(mockAccount);

        /* Then */
        Assert.assertEquals(accountId, testTransaction.getAccountId());
    }


    //region Comparation tests
    //==========================================================================
    @Test
    public void givenSelf_whenIsEquals_thenTrue() {
        Assert.assertTrue(testTransaction.equals(testTransaction));  // see [ASSERTEQUALS]
    }

    @Test
    public void givenSameTransactionButDifferentObjects_whenIsEquals_thenTrue() {
        double amount = 500.0;
        LocalDate date = LocalDate.now();
        String description = "Initial income";

        /* Given */
        Transaction testTransaction1 = new Transaction(mockAccount, amount, date, description);
        testTransaction1.setId(1);
        Transaction testTransaction2 = new Transaction(mockAccount, amount, date, description);
        testTransaction2.setId(1);

        /* Then */
        Assert.assertTrue(testTransaction1.equals(testTransaction2));  // see [ASSERTEQUALS]
        Assert.assertTrue(testTransaction2.equals(testTransaction1));  // see [ASSERTEQUALS]
    }

    @Test
    public void givenNull_whenIsEquals_thenFalse() {
        Assert.assertFalse(testTransaction.equals(null));  // see [ASSERTEQUALS]
    }

    @Test
    public void givenStrangeObject_whenIsEquals_thenFalse() {
        Assert.assertFalse(testTransaction.equals(""));  // see [ASSERTEQUALS]
    }

    @Test
    public void givenOutdatedTransaction_whenIsEquals_thenFalse() {

        /* Given an instance with same ID but has different data */
        double amount1 = 500.0;
        double amount2 = 400.0;
        LocalDate date = LocalDate.now();
        String description = "Initial income";

        Transaction testTransaction1 = new Transaction(mockAccount, amount1, date, description);
        testTransaction1.setId(1);
        Transaction testTransaction2 = new Transaction(mockAccount, amount2, date, description);
        testTransaction2.setId(1);

        /* Then */
        Assert.assertFalse(testTransaction1.equals(testTransaction2));  // see [ASSERTEQUALS]
        Assert.assertFalse(testTransaction2.equals(testTransaction1));  // see [ASSERTEQUALS]
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
