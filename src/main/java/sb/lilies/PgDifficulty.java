package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class PgDifficulty implements Difficulty {

    private final DataSource ds;
    private final int rating;

    public PgDifficulty(DataSource ds, int rating) {
        this.ds = ds;
        this.rating = rating;
    }

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
