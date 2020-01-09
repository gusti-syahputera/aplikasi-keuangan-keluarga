package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Transaction;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


public class TransactionDbHelper extends Database implements DbHelper {

    public void dropTable() {
        this.sql(Transaction.dropTableQuery).execute();
    }

    public void createTable() {
        this.sql(Transaction.createTableQuery).execute();
    }

    @Override  // to specify the table to operate on
    public Query where(String where, Object... args) {
        return new Query(this)
                .table(Transaction.tableName)
                .where(where, args);
    }

    public Transaction getTransaction(int txId) {
        Query query = this.where(Transaction.whereKeyClause, txId);
        return query.first(Transaction.class);
    }

    private String prepareMultipleAccountIdsWhereStatement(int[] accountIds) {
        String statement = "(? IS NULL OR description LIKE ?)\n" +
                           "AND (? IS NULL OR date_ >= ?)\n" +
                           "AND (? IS NULL OR date_ <= ?)\n" +
                           "AND (? IS NULL OR amount >= ?)\n" +
                           "AND (? IS NULL OR amount <= ?)\n";

        /* Generates comma separated string of the array if it's not empty */
        if (accountIds != null && accountIds.length != 0) {
            String accountIdsList = Arrays
                    .stream(accountIds)
                    .mapToObj(String::valueOf)
                    .reduce((a, b) -> a.concat(",").concat(b))
                    .get();
            statement += "AND account_id IN (#)";
            statement = statement.replace("#", accountIdsList);
        }

        return statement;
    }

    public List<Transaction> searchTransaction(int[] accountId,
                                               Integer lowerAmount, Integer upperAmount,
                                               LocalDate startDate, LocalDate endDate,
                                               String description) {
        Query query = this.where(
                prepareMultipleAccountIdsWhereStatement(accountId),
                description, description,
                startDate,   startDate,
                endDate,     endDate,
                lowerAmount, lowerAmount,
                upperAmount, upperAmount
        );
        return query.results(Transaction.class);
    }
}