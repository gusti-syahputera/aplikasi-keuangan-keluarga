package aplikasi.keuangan.keluarga;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseWrapperTest {

    private DatabaseWrapper db_wrapper;

    @Before
    public void initDatabaseWrapper() {
        String db_filename = "test_aplikasi_keuangan_keluarga.db";

        try {
            db_wrapper = new DatabaseWrapper(db_filename);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetConnection() {
        Connection connection = db_wrapper.getConnection();

        Assert.assertNotEquals(null, connection);
    }

    @Test
    public void testGetSingleValue() {
        /* Preparation */
        String message = "Hello, World!";
        String query = String.format("SELECT '%s'", message);
        String output = null;

        /* Execution */
        try {
            output = (String) db_wrapper.getSingleValue(query);
        } catch (SQLException e) {
            System.out.print("Connection attempt failed!");
        }

        /* Verification */
        Assert.assertEquals(message, output);
    }

    @Test
    public void testGetTableModel() {
        /* Preparation */
        String query = "SELECT 'Hello, World!'";
        DefaultTableModel output = null;

        /* Execution */
        try {
            output = db_wrapper.getTableModel(query);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }

        /* Verification */
        Assert.assertEquals(1, output.getRowCount());
    }

}
