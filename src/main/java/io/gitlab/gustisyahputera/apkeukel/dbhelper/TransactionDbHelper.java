package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Transaction;

import java.time.LocalDate;
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

    public List<Transaction> searchTransaction(int[] accountIds,
                                               Double lowerAmount,
                                               Double upperAmount,
                                               LocalDate startDate,
                                               LocalDate endDate,
                                               String description) {
        return searchTransaction(accountIds, lowerAmount, upperAmount, startDate, endDate, description,
                                 Transaction.transactionIdColumn, true);
    }

    /** Search Transaction with ordering parameters */
    public List<Transaction> searchTransaction(int[] accountIds,
                                               Double lowerAmount,
                                               Double upperAmount,
                                               LocalDate startDate,
                                               LocalDate endDate,
                                               String description,
                                               String orderBy,
                                               boolean ascendingOrder) {
        return searchTransaction(accountIds, lowerAmount, upperAmount, startDate, endDate, description,
                                 orderBy, ascendingOrder, 0, Integer.MAX_VALUE);
    }

    /** Search Transaction with ordering and pagination parameters */
    public List<Transaction> searchTransaction(int[] accountIds,
                                               Double lowerAmount,
                                               Double upperAmount,
                                               LocalDate startDate,
                                               LocalDate endDate,
                                               String description,
                                               String orderBy,
                                               boolean ascendingOrder,
                                               Object offset,
                                               Integer limit) {
        Query query = this.sql(

                /* Select clauses */
                "SELECT * FROM transaction_\n" +
                "WHERE (? IS NULL OR description LIKE ?)\n" +
                "AND (? IS NULL OR date_ >= ?)\n" +
                "AND (? IS NULL OR date_ <= ?)\n" +
                "AND (? IS NULL OR amount >= ?)\n" +
                "AND (? IS NULL OR amount <= ?)\n" +
                generateMembershipWhereClauses(Transaction.accountIdColumn, accountIds) +

                /* Ordering & pagination clauses */
                generateOrderingAndPaginatingClauses(orderBy, ascendingOrder),

                /* Select parameters */
                description, description,
                startDate,   startDate,
                endDate,     endDate,
                lowerAmount, lowerAmount,
                upperAmount, upperAmount,

                /* Ordering & pagination parameters */
                offset, limit
        );
        return query.results(Transaction.class);
    }
}