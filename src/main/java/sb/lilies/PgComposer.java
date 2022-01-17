package sb.lilies;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PgComposer implements Author {

    private final DataSource ds;
    private final int id;

    @Override
    public String name() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT last_name FROM author WHERE author_id = ?")
                .set(id)
                .select(new SingleOutcome<>(String.class));
    }
}
