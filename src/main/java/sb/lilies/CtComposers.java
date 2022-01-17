package sb.lilies;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CtComposers implements Authors {

    private final DataSource ds;

    @Override
    public Iterable<Author> iterate() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT author_id, last_name FROM author")
                .select(new ListOutcome<>(
                                rset -> new CtComposer(
                                        new PgComposer(ds, rset.getInt(1)),
                                        rset.getString(2)
                                )
                        )
                );
    }

    @Override
    public Iterable<Author> forSheet(int id) throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT author_id, last_name FROM author WHERE author_id = (SELECT composer_id FROM music_composer WHERE music_id = ?)")
                .set(id)
                .select(new ListOutcome<>(
                                rset -> new CtComposer(
                                        new PgComposer(ds, rset.getInt(1)),
                                        rset.getString(2)
                                )
                        )
                );
    }

}
