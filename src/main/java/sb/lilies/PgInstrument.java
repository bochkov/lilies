package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class PgInstrument implements Instrument {

    private final DataSource ds;
    private final long id;

    public PgInstrument(DataSource ds, long id) {
        this.ds = ds;
        this.id = id;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String name() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT name FROM instrument WHERE instrument_id = ?")
                .set(this.id)
                .select(new SingleOutcome<>(String.class));
    }

    @Override
    public String slug() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT slug FROM instrument WHERE instrument_id = ?")
                .set(this.id)
                .select(new SingleOutcome<>(String.class));
    }
}
