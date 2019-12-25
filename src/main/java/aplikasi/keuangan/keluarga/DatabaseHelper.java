package aplikasi.keuangan.keluarga;

import java.sql.*;


public class DatabaseHelper {


    /* ==== Constructor ===================================================== */

    public DatabaseHelper(String db_filename) throws SQLException {
        this.db_filename = db_filename;
        checkConnection();  // throws SQLException if failed
        initializeDb();
    }


    /* ==== Properties ====================================================== */

    private String db_filename;

    public String getDbFilename() {
        return this.db_filename;
    }

    public String getURL() {
        return "jdbc:sqlite:" + this.db_filename;
    }


    /* ==== Internal methods ================================================ */

    private void checkConnection() throws SQLException {
        DriverManager.getConnection(this.getURL());
    }
    
    private void initializeDb() {

        /* TODO: Improve this rather not elegant database initializer */

        String[] create_tables = {

            "CREATE TABLE IF NOT EXISTS member (" +
            "       member_id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "       full_name  TEXT    NOT NULL," +
            "       birth_date TEXT," +
            "       role       INTEGER DEFAULT 0," +  // Default: Role.ORDINARY
            "       pass_key   TEXT    DEFAULT NULL" +
            ");",

            "CREATE TABLE IF NOT EXISTS account (" +
            "       account_id   INTEGER PRIMARY KEY NOT NULL," +
            "       account_name TEXT    NOT NULL," +
            "       owner_id     INTEGER NOT NULL," +
            "       balance      INTEGER NOT NULL," +
            "       FOREIGN KEY (owner_id) REFERENCES member(member_id)" +
            "         ON DELETE SET NULL" +
            ");",

            "CREATE TABLE IF NOT EXISTS transaction_ (" +
            "       tx_id       INTEGER PRIMARY KEY NOT NULL," +
            "       account_id  INTEGER NOT NULL," +
            "       amount      INTEGER NOT NULL," +
            "       date_       TEXT NOT NULL," +
            "       description TEXT," +
            "       FOREIGN KEY (account_id) REFERENCES account(account_id)" +
            "         ON DELETE SET NULL" +
            ");"
        };

        for (String query: create_tables) {
            Connection db_connection = this.newConnection();
            try {
                Statement statement = db_connection.createStatement();
                statement.execute(query);
                statement.close();
            } catch (SQLException e) {
                /* TODO: Use the logger */
                e.printStackTrace();
            } finally {
                try {
                    db_connection.close();
                } catch (SQLException e) {  // o_O
                    e.printStackTrace();
                }
            }
        }
    }


    /* ==== Public methods ================================================== */

    public Connection newConnection() {
        try {
            return DriverManager.getConnection(this.getURL());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* TODO: Add a method that clean up connection & statement */

}
