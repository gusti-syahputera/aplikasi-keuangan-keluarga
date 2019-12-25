package access.control;

import aplikasi.keuangan.keluarga.AplikasiKeuanganKeluarga;
import aplikasi.keuangan.keluarga.FamilyMember;
import de.rtner.security.auth.spi.SimplePBKDF2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessControl {

    private FamilyMember identity;

    /**
     * Access control model for an authenticated user
     */
    public AccessControl(FamilyMember candidate, String password) throws AuthFailException {
        Connection db_connection = AplikasiKeuanganKeluarga.getDbHelper().newConnection();

        PreparedStatement statement;
        String pass_key;
        try {
            /* Create statement template */
            String query;
            query  = "SELECT pass_key";
            query += "  FROM member";
            query += " WHERE member_id = ?";  // 1
            statement = db_connection.prepareStatement(query);

            /* Fill statement template */
            statement.setInt(1, candidate.getId());

            /* Retreive the member's pass key from the database */
            ResultSet rs = statement.executeQuery();
            pass_key = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuthFailException();
        }

        /* Verify user provided password */
        boolean is_key_verified = new SimplePBKDF2().verifyKeyFormatted(pass_key, password);
        if (!is_key_verified) {
            throw new AuthFailException();
        } else {
            this.identity = candidate;
        }
    }

    public FamilyMember getIdentity() {
        return this.identity;
    }

    public FamilyMember.Role getUserRole() {
        return this.identity.getRole();
    }

    /* TODO: Add access controled methods */
}
