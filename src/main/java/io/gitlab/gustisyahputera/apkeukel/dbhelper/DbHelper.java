package io.gitlab.gustisyahputera.apkeukel.dbhelper;


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
}
