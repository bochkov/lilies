package sb.lilies;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PgDifficulties implements Difficulties {

    private final DataSource ds;

    @Override
    public int maxValue() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT MAX(rating) FROM difficulty")
                .select(new SingleOutcome<>(Long.class))
                .intValue();
    }

    @Override
    public Iterable<Difficulty> iterate() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT rating FROM difficulty")
                .select(new ListOutcome<>(rset -> new PgDifficulty(ds, rset.getInt(1))));
    }
}
