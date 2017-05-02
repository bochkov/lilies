package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class PgComposer implements Author {

    private final int id;
    private final DataSource ds;

    public PgComposer(DataSource ds, int id) {
        this.ds = ds;
        this.id = id;
    }

    @Override
    public String name() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT last_name FROM author WHERE author_id = ?")
                .set(id)
                .select(new SingleOutcome<>(String.class));
    }
}
