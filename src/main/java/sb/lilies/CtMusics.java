package sb.lilies;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CtMusics implements Musics {

    private final DataSource ds;

    private Music musicMap(ResultSet rset) throws SQLException {
        return new CtMusic(
                new PgMusic(ds, rset.getInt(1)),
                rset.getString(2),
                rset.getString(3),
                new CtStorage(
                        new PgStorage(ds, rset.getInt(4)),
                        rset.getString(5)
                ),
                new CtDifficulty(
                        new PgDifficulty(ds, rset.getInt(6)),
                        rset.getString(7)
                )
        );
    }

    @Override
    public Iterable<Music> iterate() throws SQLException {
        return new JdbcSession(this.ds)
                .sql("SELECT m.music_id, m.name, m.subname, m.storage_id, s.filename, m.difficulty, d.name FROM music m " +
                        "LEFT JOIN storage s ON m.storage_id = s.storage_id " +
                        "LEFT JOIN difficulty d ON m.difficulty = d.rating")
                .select(
                        new ListOutcome<>(this::musicMap)
                );
    }

    @Override
    public long count() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT count(*) FROM music")
                .select(new SingleOutcome<>(Long.class));
    }

    @Override
    public Music find(int id) throws SQLException {
        return new JdbcSession(this.ds)
                .sql("SELECT m.music_id, m.name, m.subname, m.storage_id, s.filename, m.difficulty, d.name FROM music m " +
                        "LEFT JOIN storage s ON m.storage_id = s.storage_id " +
                        "LEFT JOIN difficulty d ON m.difficulty = d.rating " +
                        "WHERE m.music_id = ?")
                .set(id)
                .select(
                        new ListOutcome<>(this::musicMap)
                ).get(0);
    }

    @Override
    public Iterable<Music> search(String token) throws SQLException {
        String tk = String.format("%%%s%%", token);
        return new JdbcSession(ds)
                .sql("SELECT m.music_id, m.name, m.subname, m.storage_id, s.filename, m.difficulty, d.name FROM music m " +
                        "LEFT JOIN storage s ON m.storage_id = s.storage_id " +
                        "LEFT JOIN difficulty d ON m.difficulty = d.rating " +
                        "LEFT JOIN music_composer mc ON m.music_id = mc.music_id " +
                        "LEFT JOIN music_writer mw ON m.music_id = mw.music_id " +
                        "LEFT JOIN author a ON a.author_id = mc.composer_id OR a.author_id = mw.writer_id " +
                        "WHERE UPPER(m.name) LIKE UPPER(?) " +
                        "OR UPPER(m.subname) LIKE UPPER(?) " +
                        "OR UPPER(a.last_name) LIKE UPPER(?)")
                .set(tk).set(tk).set(tk)
                .select(
                        new ListOutcome<>(this::musicMap)
                );
    }
}
