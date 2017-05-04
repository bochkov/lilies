package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class PgMusics implements Musics {

    private final DataSource ds;

    public PgMusics(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Iterable<Music> iterate() throws SQLException {
        return new JdbcSession(this.ds)
                .sql("SELECT music_id FROM music")
                .select(
                        new ListOutcome<>(
                                (ListOutcome.Mapping<Music>) rset -> new PgMusic(ds, rset.getInt(1))));
    }

    @Override
    public long count() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT count(*) FROM music")
                .select(new SingleOutcome<>(Long.class));
    }

    @Override
    public Music find(int id) throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT music_id FROM music WHERE music_id = ?")
                .set(id)
                .select(
                        new ListOutcome<>(
                                (ListOutcome.Mapping<Music>) rset -> new PgMusic(ds, rset.getInt(1))
                        )
                ).get(0);
    }

    @Override
    public Iterable<Music> search(String token) throws SQLException {
        String tk = String.format("%%%s%%", token);
        return new JdbcSession(ds)
                .sql("SELECT m.music_id FROM music m " +
                        "LEFT JOIN music_composer mc on m.music_id = mc.music_id " +
                        "LEFT JOIN music_writer mw on m.music_id = mw.music_id " +
                        "LEFT JOIN author a on a.author_id = mc.composer_id or a.author_id = mw.writer_id " +
                        "WHERE UPPER(m.name) LIKE UPPER(?) " +
                        "OR UPPER(m.subname) LIKE UPPER(?) " +
                        "OR UPPER(a.last_name) LIKE UPPER(?)")
                .set(tk).set(tk).set(tk)
                .select(new ListOutcome<>(
                        (ListOutcome.Mapping<Music>) rset -> new PgMusic(ds, rset.getInt(1))));
    }
}
