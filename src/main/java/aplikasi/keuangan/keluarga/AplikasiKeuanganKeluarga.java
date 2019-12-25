package aplikasi.keuangan.keluarga;

import access.control.AccessControl;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AplikasiKeuanganKeluarga {
    private static DatabaseHelper db_helper;
    private static AccessControl access_control;
    private static Logger logger;

    public static void main(String[] args) {
        logger = Logger.getLogger(AplikasiKeuanganKeluarga.class.getName());

        logger.log(Level.INFO, "Attempting to connect to the database...");

        String db_filename = args[0];
        try {
            db_helper = new DatabaseHelper(db_filename);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection attempt failed!");
        }

        logger.log(Level.FINE, "Main program finished.");
    }

    public static DatabaseHelper getDbHelper() {
        return db_helper;
    }

    public static AccessControl getAccessControl() {
        return access_control;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void assignAccessControl(AccessControl ac) {
        access_control = ac;
    }
}
