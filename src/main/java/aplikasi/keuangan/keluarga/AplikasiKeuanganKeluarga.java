package aplikasi.keuangan.keluarga;

import java.sql.Connection;
import java.sql.SQLException;

public class AplikasiKeuanganKeluarga {
    private static DatabaseWrapper db_wrapper;
    private static Connection db_connection;

    public static void main(String[] args) {
        System.out.println("Connecting to database...");

        String db_filename = args[0];
        try {
            db_wrapper = new DatabaseWrapper(db_filename);
        } catch (SQLException e) {
            System.out.println("Connection attempt failed!");
        }
    }

    public static DatabaseWrapper getDatabaseWrapper() {
        return db_wrapper;
    }

    public static Connection getDatabaseConnection() {
        return db_wrapper.getConnection();
    }
}
