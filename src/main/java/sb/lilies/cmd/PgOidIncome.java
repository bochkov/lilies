package sb.lilies.cmd;

import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public final class PgOidIncome {

    private final byte[] buf =  new byte[2048];

    private final DataSource ds;
    private final File file;

    public PgOidIncome(DataSource ds, File file) {
        this.ds = ds;
        this.file = file;
    }

    public long id() throws SQLException, IOException {
        Connection con = ds.getConnection();
        con.setAutoCommit(false);
        LargeObjectManager lobj = con.unwrap(PGConnection.class).getLargeObjectAPI();
        long oid = lobj.createLO(LargeObjectManager.READWRITE);
        LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
        try (FileInputStream fis = new FileInputStream(file)) {
            int s;
            while ((s = fis.read(buf)) > 0) {
                obj.write(buf, 0, s);
            }
            obj.close();
            con.commit();
            return oid;
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        }
    }
}
