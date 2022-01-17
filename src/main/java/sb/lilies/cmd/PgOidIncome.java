package sb.lilies.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

@RequiredArgsConstructor
public final class PgOidIncome {

    private final byte[] buf = new byte[2048];

    private final DataSource ds;
    private final File file;

    public long id() throws SQLException, IOException {
        try (Connection con = ds.getConnection()) {
            con.setAutoCommit(false);
            LargeObjectManager lom = con.unwrap(PGConnection.class).getLargeObjectAPI();
            long oid = lom.createLO(LargeObjectManager.READWRITE);
            try (LargeObject obj = lom.open(oid, LargeObjectManager.WRITE);
                 FileInputStream fis = new FileInputStream(file)) {
                int s;
                while ((s = fis.read(buf)) > 0) {
                    obj.write(buf, 0, s);
                }
                con.commit();
                return oid;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }
}
