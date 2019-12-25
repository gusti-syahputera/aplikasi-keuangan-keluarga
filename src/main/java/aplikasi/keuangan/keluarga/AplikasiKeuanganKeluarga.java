package aplikasi.keuangan.keluarga;

import access.control.AccessControl;

import java.sql.SQLException;

public class AplikasiKeuanganKeluarga {
    private static DatabaseHelper db_helper;
    private static AccessControl access_control;

    public static void main(String[] args) {
        System.out.println("Main program started.");

        String db_filename = args[0];
        try {
            db_helper = new DatabaseHelper(db_filename);
        } catch (SQLException e) {
            System.out.println("Database connection attempt failed!");
        }

        System.out.println("Main program finished.");
    }

    public static DatabaseHelper getDbHelper() {
        return db_helper;
    }

    public static AccessControl getAccessControl() {
        return access_control;
    }

    public static void assignAccessControl(AccessControl ac) {
        access_control = ac;
    }
}
