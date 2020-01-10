package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Account;

import java.util.List;


public class AccountDbHelper extends Database implements DbHelper {

    public void dropTable() {
        this.sql(Account.dropTableQuery).execute();
    }

    public void createTable() {
        this.sql(Account.createTableQuery).execute();
    }

    @Override  // to specify the table to operate on
    public Query where(String where, Object... args) {
        return new Query(this)
                .table(Account.tableName)
                .where(where, args);
    }

    public Account getAccount(int accountId) {
        Query query = this.where(Account.whereKeyClause, accountId);
        return query.first(Account.class);
    }

    public List<Account> searchAccount(String name, int[] ownerIds) {
        return searchAccount(name, ownerIds, Account.accountIdColumn, true);
    }

    /** Search Account with ordering parameters */
    public List<Account> searchAccount(String name,
                                       int[] ownerIds,
                                       String orderBy,
                                       boolean ascendingOrder) {
        return searchAccount(name, ownerIds, orderBy, ascendingOrder, 0, Integer.MAX_VALUE);
    }

    /** Search Account with ordering and pagination parameters */
    public List<Account> searchAccount(String name,
                                       int[] ownerIds,
                                       String orderBy,
                                       boolean ascendingOrder,
                                       Object offset,
                                       Integer limit) {
        Query query = this.sql(

                /* Select clauses */
                "SELECT * FROM account\n" +
                "WHERE (? IS NULL OR account_name LIKE ?)\n" +
                generateMembershipWhereClauses(Account.ownerIdColumn, ownerIds) +

                /* Ordering & pagination clauses */
                generateOrderingAndPaginatingClauses(orderBy, ascendingOrder),

                /* Select parameters */
                name, name,

                /* Ordering & pagination parameters */
                offset, limit
        );
        return query.results(Account.class);
    }

}
