package sb.lilies;

import com.jcabi.jdbc.Outcome;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class PgOidOutcome implements Outcome<byte[]> {

    @Override
    public byte[] handle(ResultSet rset, Statement stmt) throws SQLException {
        Connection con = stmt.getConnection();
        con.setAutoCommit(false);
        LargeObjectManager lobj = con.unwrap(PGConnection.class).getLargeObjectAPI();
        byte[] buf = new byte[0];
        if (rset.next()) {
            try (LargeObject obj = lobj.open(rset.getLong(1), LargeObjectManager.READ)) {
                buf = obj.read(obj.size());
            }
        }
        con.commit();
        return buf;
    }
}
