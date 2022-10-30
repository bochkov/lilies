package sb.lilies.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PgOidIncome {

    private final byte[] buf = new byte[2048];

    private final DataSource ds;
    private final File file;

    public long id() throws SQLException, IOException {
        return 0L;
    }
}
