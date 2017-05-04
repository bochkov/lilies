package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class CtDifficulties implements Difficulties {

    private final DataSource ds;

    public CtDifficulties(DataSource ds) {
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
                .sql("SELECT rating, name FROM difficulty")
                .select(new ListOutcome<>(
                        (ListOutcome.Mapping<Difficulty>) rset ->
                                new CtDifficulty(
                                        new PgDifficulty(ds, rset.getInt(1)),
                                        rset.getString(2)
                                )));
    }
}
