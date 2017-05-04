package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class CtInstruments implements Instruments {

    private final DataSource ds;

    public CtInstruments(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public long count() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT count(*) FROM instrument")
                .select(new SingleOutcome<>(Long.class));
    }

    @Override
    public Instrument find(String slug) throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT instrument_id, name, slug FROM instrument WHERE slug = ?")
                .set(slug)
                .select(new ListOutcome<>(
                        (ListOutcome.Mapping<Instrument>) rset ->
                                new CtInstrument(
                                        new PgInstrument(ds, rset.getLong(1)),
                                        rset.getString(2),
                                        rset.getString(3)))
                ).get(0);
    }

    @Override
    public Iterable<Instrument> iterate() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT instrument_id, name, slug FROM instrument")
                .select(new ListOutcome<>(
                        (ListOutcome.Mapping<Instrument>) rset ->
                                new CtInstrument(
                                        new PgInstrument(ds, rset.getLong(1)),
                                        rset.getString(2),
                                        rset.getString(3))));
    }

    @Override
    public Iterable<Instrument> forSheet(int id) throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT instrument_id, name, slug FROM instrument WHERE instrument_id IN (SELECT instrument_id FROM music_instrument WHERE music_id = ?)")
                .set(id)
                .select(new ListOutcome<>(
                        (ListOutcome.Mapping<Instrument>) rset ->
                                new CtInstrument(
                                        new PgInstrument(ds, rset.getInt(1)),
                                        rset.getString(2),
                                        rset.getString(3))));
    }
}
