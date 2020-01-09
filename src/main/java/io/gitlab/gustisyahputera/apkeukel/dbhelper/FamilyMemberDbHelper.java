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
                                                 LocalDate startBod, LocalDate endBod,
                                                 Role role) {
        Integer roleOrd = (role == null) ? null : role.ordinal();
        Query query = this.where(
                    "(? IS NULL OR full_name LIKE ?)\n" +
                    "AND (? IS NULL OR birth_date >= ?)\n" +
                    "AND (? IS NULL OR birth_date <= ?)\n" +
                    "AND (? IS NULL OR role = ?)",
                    name, name, startBod, startBod, endBod, endBod, roleOrd, roleOrd
            );
        return query.results(FamilyMember.class);
    }

}
