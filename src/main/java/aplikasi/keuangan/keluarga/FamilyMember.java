package aplikasi.keuangan.keluarga;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

public class FamilyMember {


    public enum Role {ORDINARY, ACCOUNTANT, CHIEF}


    /* ==== Constructor ===================================================== */

    /**
     * Construct member model.
     *
     * The particular member is assumed as already exists in the
     * database.
     */
    public FamilyMember(int member_id) throws SQLException {
        this.member_id = member_id;

        /* Pull member data from database */
        this.pullData();
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

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public void setBirthDate(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRole(int role_number) {
        this.role = Role.values()[role_number];
    }


    /* ==== Database CRUD methods =========================================== */

    /* Create */

    /**
     * Create new member.
     *
     * This static method adds a new member into the database.
     * Then it returns the new member model.
     */
    public static FamilyMember newMember(String full_name,
                                         LocalDate birth_date,
                                         Role role) throws SQLException {
        Connection db_connection = AplikasiKeuanganKeluarga.getDbHelper().newConnection();

        /* Create statement template */
        String query;
        query  = "INSERT INTO member";
        query += "     VALUES (NULL, ?, ?, ?)";
        PreparedStatement statement = db_connection.prepareStatement(query);

        /* Fill statement template */
        statement.setString(1, full_name);
        statement.setString(2, birth_date.toString());  // see note [SQLDATE]
        statement.setInt(3, role.ordinal());
        statement.executeUpdate();

        /* Retreive the generated auto incremented id value */
        ResultSet rs = statement.getGeneratedKeys();
        int member_id = rs.getInt(1);

        statement.close();
        db_connection.close();
        return new FamilyMember(member_id);
    }


    /* Read */

    /**
     * Pull member data from database.
     */
    public void pullData() throws SQLException {
        /* Create statement template */
        Connection db_connection = AplikasiKeuanganKeluarga.getDbHelper().newConnection();
        String query;
        query  = "SELECT full_name,";
        query += "       birth_date,";
        query += "       role";
        query += "  FROM member";
        query += " WHERE member_id = ?";  // 1
        PreparedStatement statement = db_connection.prepareStatement(query);

        /* Fill statement template */
        statement.setInt(1, this.member_id);

        /* Execute select query */
        ResultSet rs = statement.executeQuery();

        /* Parse the result set and update self properties */
        this.full_name = rs.getString(1);
        this.birth_date = LocalDate.parse(rs.getString(2));
        this.role = Role.values()[rs.getInt(3)];

        statement.close();
        db_connection.close();
    }


    /* Update */

    /**
     * Push member data to the database.
     *
     * This method will be invoked when user click "Save" button in
     * member data update form.
     */
    public void pushData() throws SQLException {
        /* Create statement template */
        Connection db_connection = AplikasiKeuanganKeluarga.getDbHelper().newConnection();
        String query;
        query  = "UPDATE member";
        query += "    SET full_name = ?,";   // 1
        query += "        birth_date = ?,";  // 2
        query += "        role = ?";         // 3
        query += "  WHERE member_id = ?";    // 4
        PreparedStatement statement = db_connection.prepareStatement(query);

        /* Fill statement template */
        statement.setString(1, this.full_name);
        statement.setString(2, this.birth_date.toString());  // see note [SQLDATE]
        statement.setInt(3, this.role.ordinal());
        statement.setInt(4, this.member_id);

        /* Execute query */
        statement.executeUpdate();  // may throw SQLException

        statement.close();
        db_connection.close();
    }


    /* Delete */

    /**
     * Remove specific member from the database.
     */
    public static void removeMember(int member_id) throws SQLException {
        /* Create statement template */
        Connection db_connection = AplikasiKeuanganKeluarga.getDbHelper().newConnection();
        String query;
        query  = "DELETE FROM member";
        query += "      WHERE member_id = ?";  // 1
        PreparedStatement statement = db_connection.prepareStatement(query);

        /* Fill statement template */
        statement.setInt(1, member_id);

        /* Execute query */
        statement.executeUpdate();  // may throw SQLException

        statement.close();
        db_connection.close();
    }

}

/* TODO: Fix SQLite connection mechanism */

/*
 * Notes
 *
 * [SQLDATE] SQLite don't have DATE, therefore ISO8601 string is used instead
 */
