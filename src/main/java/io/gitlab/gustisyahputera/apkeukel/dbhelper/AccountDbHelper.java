package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


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

    public List<Account> searchAccount(String name, Integer ownerId) {
        Query query = this.where(
                "(? IS NULL OR account_name LIKE ?)\n" +
                        "AND (? IS NULL OR owner_id = ?)",
                name, name, ownerId, ownerId
        );
        return query.results(Account.class);
    }

    private String prepareMultipleOwnerIdsWhereStatement(int... ownerIds) {

        /* Generates comma separated string of the array */
        String ownerIdsList = Arrays
                .stream(ownerIds)
                .mapToObj(String::valueOf)
                .reduce((a, b) -> a.concat(",").concat(b))
                .get();

        String statement = "(? IS NULL OR account_name LIKE ?)\n" +
                           "AND owner_id IN (#)";
        return statement.replace("#", ownerIdsList);
    }

    public List<Account> searchAccount(String name, int... ownerIds) {
        Query query = this.where(
                prepareMultipleOwnerIdsWhereStatement(ownerIds),
                name, name
        );
        return query.results(Account.class);
    }

}
