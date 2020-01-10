package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Query;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.FamilyMember;
import io.gitlab.gustisyahputera.apkeukel.entitymodel.Role;

import java.time.LocalDate;
import java.util.List;


public class FamilyMemberDbHelper extends Database implements DbHelper {

    public void dropTable() {
        this.sql(FamilyMember.dropTableQuery).execute();
    }

    public void createTable() {
        this.sql(FamilyMember.createTableQuery).execute();
    }

    @Override  // to specify the table to operate on
    public Query where(String where, Object... args) {
        return new Query(this)
                .table(FamilyMember.tableName)
                .where(where, args);
    }

    public FamilyMember getFamilyMember(int memberId) {
        Query query = this.where(FamilyMember.whereKeyClause, memberId);
        return query.first(FamilyMember.class);
    }

    public List<FamilyMember> searchFamilyMember(String name,
                                                 LocalDate startBod,
                                                 LocalDate endBod,
                                                 Role role) {
        return searchFamilyMember(name, startBod, endBod, role,
                                  FamilyMember.memberIdColumn, true);
    }

    /** Search FamilyMember with ordering parameters */
    public List<FamilyMember> searchFamilyMember(String name,
                                                 LocalDate startBod,
                                                 LocalDate endBod,
                                                 Role role,
                                                 String orderBy,
                                                 boolean ascendingOrder) {
        return searchFamilyMember(name, startBod, endBod, role,
                                  orderBy, ascendingOrder, 0, Integer.MAX_VALUE);
    }

    /** Search FamilyMember with ordering and pagination parameters */
    public List<FamilyMember> searchFamilyMember(String name,
                                                 LocalDate startBod,
                                                 LocalDate endBod,
                                                 Role role,
                                                 String orderBy,
                                                 boolean ascendingOrder,
                                                 Object offset,
                                                 Integer limit) {
        Integer roleOrd = (role == null) ? null : role.ordinal();
        Query query = this.sql(

                /* Select clauses */
                "SELECT * FROM member\n" +
                "WHERE (? IS NULL OR full_name LIKE ?)\n" +
                "AND (? IS NULL OR birth_date >= ?)\n" +
                "AND (? IS NULL OR birth_date <= ?)\n" +
                "AND (? IS NULL OR role = ?)\n" +

                /* Ordering & pagination clauses */
                generateOrderingAndPaginatingClauses(orderBy, ascendingOrder),

                /* Select parameters */
                name, name,
                startBod, startBod,
                endBod, endBod,
                roleOrd, roleOrd,

                /* Ordering & pagination parameters */
                offset, limit
        );
        return query.results(FamilyMember.class);
    }

}
