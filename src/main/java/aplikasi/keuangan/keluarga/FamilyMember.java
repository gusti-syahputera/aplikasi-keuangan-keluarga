package aplikasi.keuangan.keluarga;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class FamilyMember {

    public enum Role {
        ORDINARY, ACCOUNTANT, CHIEF;
    }

    /* ==== Constructors ==================================================== */

    public FamilyMember(int member_id, String full_name,
                        LocalDate birth_date, Role role) {
        this.member_id = member_id;
        this.full_name = full_name;
        this.birth_date = birth_date;
        this.role = role;
    }

    /* ==== Properties ====================================================== */

    private int member_id;
    private String full_name;
    private LocalDate birth_date;
    private Role role;

    public int getId() {
        return this.member_id;
    }

    public String getFullName() {
        return this.full_name;
    }

    public LocalDate getBirthDate() {
        return this.birth_date;
    }

    public Role getRole() {
        return this.role;
    }

    public int getAge() {
        return Period.between(this.birth_date, LocalDate.now()).getYears();
    }

    /* ==== CRUD methods ==================================================== */

    /* Create */

    public static FamilyMember newMember(String full_name,
                                         LocalDate birth_date,
                                         Role role) throws SQLException {
        Connection db_connection = AplikasiKeuanganKeluarga.getDatabaseConnection();
        PreparedStatement statement = db_connection.prepareStatement(
                "INSERT INTO member VALUES (NULL, ?, ?, ?)"
        );
        statement.setString(1, full_name);
        statement.setString(2, birth_date.toString());  // see note [SQLDATE]
        statement.setInt(3, role.ordinal());
        statement.executeUpdate();

        /* Retreive the generated auto-incremented id value */
        ResultSet rs = statement.getGeneratedKeys();
        int member_id = rs.getInt(1);

        return new FamilyMember(member_id, full_name, birth_date, role);
    }

    /* Read */

    private void reloadData() throws SQLException {
        /* Execute query into database*/
        Connection db_connection = AplikasiKeuanganKeluarga.getDatabaseConnection();
        String query = (
                "SELECT full_name, birth_date, role " +
                        "FROM member WHERE member_id = ?"
        );
        PreparedStatement statement = db_connection.prepareStatement(query);
        statement.setInt(1, member_id);
        ResultSet rs = statement.executeQuery();
        this.full_name = rs.getString(1);
        this.birth_date = LocalDate.parse(rs.getString(2));
        this.role = Role.values()[rs.getInt(3)];

        /* Parse result set and update member properties */

    }

    /* Updates */

    private PreparedStatement getUpdateStatement(String column_name) throws SQLException {
        /* TODO: Check if user is priviledged to doing this action */

        /* Prepare update statement */
        Connection db_connection = AplikasiKeuanganKeluarga.getDatabaseConnection();
        String query = String.format(
                "UPDATE member SET %s = ? WHERE member_id = ?", column_name);
        PreparedStatement statement = db_connection.prepareStatement(query);
        statement.setInt(2, this.member_id);

        return statement;
    }

    public void setFullName(String new_full_name) throws SQLException {
        PreparedStatement statement = this.getUpdateStatement("full_name");
        statement.setString(1, new_full_name);
        statement.executeUpdate();
        this.reloadData();
    }

    public void setBirthDate(LocalDate new_birth_date) throws SQLException {
        PreparedStatement statement = this.getUpdateStatement("birth_date");
        statement.setString(1, new_birth_date.toString());  // see note [SQLDATE]
        statement.executeUpdate();
        this.reloadData();
    }

    public void setRole(Role new_role) throws SQLException {
        PreparedStatement statement = this.getUpdateStatement("role");
        statement.setInt(1, new_role.ordinal());
        statement.executeUpdate();
        this.reloadData();
    }

}

/*
 * Notes
 *
 * [SQLDATE] SQLite don't have DATE, therefore ISO8601 string is used instead
 */
