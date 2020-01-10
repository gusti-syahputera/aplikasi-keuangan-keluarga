package io.gitlab.gustisyahputera.apkeukel.dbhelper;

import java.util.Arrays;


public interface DbHelper {
    void dropTable();
    void createTable();

    default String generateOrderingAndPaginatingClauses(String orderBy,
                                                        boolean ascendingOrder) {
        String ordering = ascendingOrder ? "ASC" : "DESC";
        return "AND " + orderBy + " > ?\n" +  // ? = offset
               "ORDER BY " + orderBy + " " + ordering + "\n" +
               "LIMIT ?\n";  // ? = limit
    }

    default String generateMembershipWhereClauses(String columnName, int[] args) {
        String whereClauses = "";

        /* Generates comma separated string of the array if it's not empty */
        if (args != null && args.length != 0) {
            String argsList = Arrays
                    .stream(args)
                    .mapToObj(String::valueOf)
                    .reduce((a, b) -> a.concat(",").concat(b))
                    .get();
            whereClauses = "AND " + columnName + " IN (" + argsList + ")\n";
        }

        return whereClauses;
    }
}
