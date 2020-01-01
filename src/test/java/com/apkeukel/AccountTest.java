package com.apkeukel;

import com.dieselpoint.norm.Database;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class AccountTest {

    @Mock private FamilyMember familyMember = new FamilyMember();

    private static Database database;
    private Account testAccount;


    //region Setups
    //==========================================================================

    @BeforeClass
    public static void databaseSetUp() {
        database = new Database();
        database.setJdbcUrl("jdbc:sqlite:test.db");
        database.sql("DROP TABLE IF EXISTS account").execute();
        database.sql(Account.createTable).execute();
    }

    @AfterClass
    public static void databaseTearDown() {
        database.close();
    }

    @Before
    public void setUp() {
        when(familyMember.getId()).thenReturn(1);

        String accountName = "General account";
        int balance = 5000;

        /* When */
        testAccount = new Account();
        testAccount.setAccountName(accountName);
        testAccount.setOwnerId(familyMember.getId());
        testAccount.setBalance(balance);

        /* Then */
        Assert.assertEquals(accountName, testAccount.getAccountName());
        Assert.assertEquals(familyMember.getId(), testAccount.getOwnerId());
        Assert.assertEquals(balance, testAccount.getBalance());
    }
    //endregion

    @Test
    public void whenCreateWithParameterConstructor() {

        /* Given */
        String accountName = "General account";
        int balance = 5000;

        /* When */
        Account newAccount1 = new Account(accountName, familyMember, balance);
        Account newAccount2 = new Account(accountName, familyMember.getId(), balance);
        Account[] newAccounts = {newAccount1, newAccount2};

        /* Then */
        for (Account newAccount: newAccounts) {
            Assert.assertEquals(accountName, newAccount.getAccountName());
            Assert.assertEquals(familyMember.getId(), newAccount.getOwnerId());
            Assert.assertEquals(balance, newAccount.getBalance());
        }
    }

    @Test
    public void whenSetOwner() {
        /* When */
        testAccount.setOwner(familyMember);

        /* Then */
        verify(familyMember, times(3)).getId();
        Assert.assertEquals(familyMember.getId(), testAccount.getOwnerId());
    }

    @Test
    public void whenInsertToDatabase_thenGetGeneratedId() {
        /* When */
        database.insert(testAccount);

        /* Then */
        Assert.assertNotEquals(0, testAccount.getId());
    }
}
