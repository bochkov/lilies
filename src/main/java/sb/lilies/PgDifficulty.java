package sb.lilies;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PgDifficulty implements Difficulty {

    private final DataSource ds;
    private final int rating;

    @Override
    public int rating() {
        return rating;
    }

    @Override
    public String name() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT name FROM difficulty WHERE rating = ?")
                .set(this.rating)
                .select(new SingleOutcome<>(String.class));
    }
}
