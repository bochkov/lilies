package sb.lilies;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PgMusics implements Musics {

    private final DataSource ds;

    @Override
    public Iterable<Music> iterate() throws SQLException {
        return new JdbcSession(this.ds)
                .sql("SELECT music_id FROM music")
                .select(
                        new ListOutcome<>(
                                rset -> new PgMusic(ds, rset.getInt(1))
                        )
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
        return new JdbcSession(ds)
                .sql("SELECT music_id FROM music WHERE music_id = ?")
                .set(id)
                .select(new ListOutcome<>(rset -> new PgMusic(ds, rset.getInt(1))))
                .get(0);
    }

    @Override
    public Iterable<Music> search(String token) throws SQLException {
        String tk = String.format("%%%s%%", token);
        return new JdbcSession(ds)
                .sql("select * from music m " +
                        "left join music_composer mc on m.music_id = mc.music_id " +
                        "left join music_writer mw on m.music_id = mw.music_id " +
                        "left join author a1 on mc.composer_id = a1.author_id " +
                        "left join author a2 on mw.writer_id = a2.author_id " +
                        " where upper(m.name) like upper(?) " +
                        "    or upper(m.subname) like  upper(?) " +
                        " or upper(a1.last_name) like upper(?) " +
                        " or upper(a2.last_name) like upper(?) " +
                        "order by 1")
                .set(tk).set(tk).set(tk).set(tk)
                .select(new ListOutcome<>(rset -> new PgMusic(ds, rset.getInt(1))));
    }
}
