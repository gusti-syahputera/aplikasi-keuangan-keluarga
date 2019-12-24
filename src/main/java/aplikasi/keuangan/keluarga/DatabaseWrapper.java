package aplikasi.keuangan.keluarga;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;


public class DatabaseWrapper {

    private Connection connection;
    private Statement statement;

    public DatabaseWrapper(String db_filename) throws SQLException {

        String url = "jdbc:sqlite:" + db_filename;

        try {
            this.connection = DriverManager.getConnection(url);
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public Object getSingleValue(String query) throws SQLException {
        try (ResultSet rs = statement.executeQuery(query)) {
            return rs.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public DefaultTableModel getTableModel(String query) throws SQLException {

        /* Execute query and retreive the result set */
        ResultSet rs;
        ResultSetMetaData rs_meta;
        try {
            rs = statement.executeQuery(query);
            rs_meta = rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        /* Retreive column names */
        Vector<String> column_names = new Vector<>();
        int column_count = rs_meta.getColumnCount();
        for (int i = 1; i <= column_count; i++) {
            column_names.add(rs_meta.getColumnName(i));
        }

        /* Retreive rows */
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int column_i = 1; column_i < column_count; column_i++) {
                row.add(rs.getObject(column_i));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, column_names);
    }

}
