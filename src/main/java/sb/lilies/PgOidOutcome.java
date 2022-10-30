package sb.lilies;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jcabi.jdbc.Outcome;


public final class PgOidOutcome implements Outcome<byte[]> {

    @Override
    public byte[] handle(ResultSet rset, Statement stmt) throws SQLException {
        Connection con = stmt.getConnection();
        con.setAutoCommit(false);
        byte[] buf = new byte[0];
        /*
        LargeObjectManager lobj = con.unwrap(PGConnection.class).getLargeObjectAPI();
        if (rset.next()) {
            try (LargeObject obj = lobj.open(rset.getLong(1), LargeObjectManager.READ)) {
                buf = obj.read(obj.size());
            }
        }

         */
        con.commit();
        return buf;
    }
}
