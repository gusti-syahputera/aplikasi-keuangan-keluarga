package aplikasi.keuangan.keluarga;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseHelperTest {

    private DatabaseHelper db_helper;

    @Before
    public void initDatabaseWrapper() {
        String db_filename = "test_aplikasi_keuangan_keluarga.db";

        try {
            db_helper = new DatabaseHelper(db_filename);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testNewConnection() {
        Connection connection = db_helper.newConnection();
        Assert.assertNotEquals(null, connection);
    }

}
