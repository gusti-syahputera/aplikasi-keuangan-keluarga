package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Account;

import java.util.Arrays;
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

    private String prepareMultipleOwnerIdsWhereStatement(int[] ownerIds) {
        String statement = "(? IS NULL OR account_name LIKE ?)\n";

        /* Generates comma separated string of the array if it's not empty */
        if (ownerIds != null && ownerIds.length != 0) {
            String ownerIdsList = Arrays
                    .stream(ownerIds)
                    .mapToObj(String::valueOf)
                    .reduce((a, b) -> a.concat(",").concat(b))
                    .get();
            statement += "AND owner_id IN (#)";
            statement = statement.replace("#", ownerIdsList);
        }

        return statement;
    }

    public List<Account> searchAccount(String name, int[] ownerIds) {
        Query query = this.where(
                prepareMultipleOwnerIdsWhereStatement(ownerIds),
                name, name
        );
        return query.results(Account.class);
    }

}
