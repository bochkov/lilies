package sb.lilies;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PgMusic implements Music {

    private final DataSource ds;
    private final int id;

    @Override
    public int id() {
        return id;
    }

    @Override
    public Storage storage() throws SQLException {
        return new PgStorage(ds,
                new JdbcSession(ds)
                        .sql("SELECT storage_id FROM music WHERE music_id = ?")
                        .set(this.id)
                        .select(new SingleOutcome<>(Long.class))
                        .intValue());
    }

    @Override
    public String name() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT name FROM music WHERE music_id = ?")
                .set(id)
                .select(new SingleOutcome<>(String.class));
    }

    @Override
    public String subname() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT subname FROM music WHERE music_id = ?")
                .set(id)
                .select(new SingleOutcome<>(String.class));
    }

    @Override
    public Difficulty difficulty() throws SQLException {
        return new PgDifficulty(
                ds,
                new JdbcSession(ds)
                        .sql("SELECT difficulty FROM music WHERE music_id = ?")
                        .set(id)
                        .select(new SingleOutcome<>(Long.class))
                        .intValue());
    }

    @Override
    public Iterable<Author> composer() throws SQLException {
        return new CtComposers(ds).forSheet(this.id);
    }

    @Override
    public Iterable<Author> writer() throws SQLException {
        return new CtWriters(ds).forSheet(this.id);
    }

    @Override
    public boolean withDifficulties(List<String> diffs) throws SQLException {
        return diffs.contains(String.format("%s", difficulty().rating()));
    }

    @Override
    public boolean withInstruments(List<String> slugs) throws SQLException {
        for (Instrument instrument : instrument())
            if (slugs.contains(instrument.slug()))
                return true;
        return false;
    }

    public Iterable<Instrument> instrument() throws SQLException {
        return new CtInstruments(ds).forSheet(id);
    }

    @Override
    public int compareTo(Music o) {
        try {
            return this.name().compareTo(o.name());
        } catch (SQLException e) {
            return 0;
        }
    }
}
