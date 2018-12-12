package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class CtWriters implements Authors {

    private final DataSource ds;

    public CtWriters(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Iterable<Author> iterate() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT author_id, last_name FROM author")
                .select(new ListOutcome<>(
                                rset -> new CtWriter(
                                        new PgWriter(ds, rset.getInt(1)),
                                        rset.getString(2)
                                )
                        )
                );
    }

    @Override
    public Iterable<Author> forSheet(int id) throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT author_id, last_name FROM author WHERE author_id = (SELECT writer_id FROM music_writer WHERE music_id = ?)")
                .set(id)
                .select(new ListOutcome<>(
                                rset -> new CtWriter(
                                        new PgWriter(ds, rset.getInt(1)),
                                        rset.getString(2)
                                )
                        )
                );
    }

}
