package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class PgWriters implements Authors {

    private final DataSource ds;

    public PgWriters(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Iterable<Author> iterate() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT author_id FROM author")
                .select(new ListOutcome<>(
                        (ListOutcome.Mapping<Author>) rset -> new PgWriter(ds, rset.getInt(1))));
    }

    @Override
    public Iterable<Author> forSheet(int id) throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT writer_id FROM music_writer WHERE music_id = ?")
                .set(id)
                .select(new ListOutcome<>(
                        (ListOutcome.Mapping<Author>) rset -> new PgWriter(ds, rset.getInt(1))));
    }
}
