package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class PgDifficulties implements Difficulties {

    private final DataSource ds;

    public PgDifficulties(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public long count() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT count(*) FROM difficulty")
                .select(new SingleOutcome<>(Long.class));
    }

    @Override
    public Iterable<Difficulty> iterate() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT rating FROM difficulty")
                .select(new ListOutcome<>(
                                rset -> new PgDifficulty(ds, rset.getInt(1))
                        )
                );
    }
}
