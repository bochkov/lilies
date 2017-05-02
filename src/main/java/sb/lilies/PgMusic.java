package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public final class PgMusic implements Music, Comparable {

    private final DataSource ds;
    private final int id;

    public PgMusic(DataSource ds, int id) {
        this.ds = ds;
        this.id = id;
    }

    public int id() {
        return id;
    }

    public PgStorage storage() throws SQLException {
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

    public String subname() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT subname FROM music WHERE music_id = ?")
                .set(id)
                .select(new SingleOutcome<>(String.class));
    }

    public Difficulty difficulty() throws SQLException {
        return new PgDifficulty(
                ds,
                new JdbcSession(ds)
                        .sql("SELECT difficulty FROM music WHERE music_id = ?")
                        .set(id)
                        .select(new SingleOutcome<>(Long.class))
                        .intValue());
    }

    public Iterable<Author> composer() throws SQLException {
        return new PgComposers(ds).forSheet(this.id);
    }

    public Iterable<Author> writer() throws SQLException {
        return new PgWriters(ds).forSheet(this.id);
    }

    @Override
    public boolean withDifficulties(List<String> diffs) throws SQLException {
        return diffs.contains(String.format("%s", difficulty().rating()));
    }

    @Override
    public boolean withInstruments(List<String> slugs) throws SQLException {
        List<Instrument> instruments = new JdbcSession(ds)
                .sql("select instrument_id from instrument where instrument_id in (select instrument_id from music_instrument where music_id = ?)")
                .set(this.id)
                .select(new ListOutcome<>(
                        (ListOutcome.Mapping<Instrument>) rset -> new PgInstrument(ds, rset.getInt(1))));
        for (Instrument instrument : instruments)
            if (slugs.contains(instrument.slug()))
                return true;
        return false;
    }

    public Iterable<Instrument> instrument() throws SQLException {
        return new PgInstruments(ds).forSheet(id);
    }

    public String grouper() {
        try {
            return name().substring(0, 1);
        } catch (SQLException ex) {
            return "_";
        }
    }

    public String srcFilename() throws SQLException {
        return String.format("src/%s.ly",
                new JdbcSession(ds)
                        .sql("SELECT filename FROM storage WHERE storage_id = (SELECT storage_id FROM music WHERE music_id = ?)")
                        .set(id)
                        .select(new SingleOutcome<>(String.class)));
    }

    public boolean hasSrc() throws Exception {
        File outFile = new File("media", srcFilename());
        if (!outFile.exists()) {
            storage().saveSrc(outFile);
        }
        return outFile.exists();
    }

    public String pdfFilename() throws SQLException {
        return String.format("pdf/%s.pdf",
                new JdbcSession(ds)
                        .sql("SELECT filename FROM storage WHERE storage_id = (SELECT storage_id FROM music WHERE music_id = ?)")
                        .set(id)
                        .select(new SingleOutcome<>(String.class)));
    }

    public boolean hasPdf() throws SQLException, IOException {
        File outFile = new File("media", pdfFilename());
        if (!outFile.exists()) {
            storage().savePdf(outFile);
        }
        return outFile.exists();
    }

    public String mp3Filename() throws SQLException {
        return String.format("mp3/%s.mp3",
                new JdbcSession(ds)
                        .sql("SELECT filename FROM storage WHERE storage_id = (SELECT storage_id FROM music WHERE music_id = ?)")
                        .set(id)
                        .select(new SingleOutcome<>(String.class)));
    }

    public boolean hasMp3() throws SQLException, IOException {
        File outFile = new File("media", mp3Filename());
        if (!outFile.exists()) {
            storage().saveMp3(outFile);
        }
        return outFile.exists();
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Music) {
            try {
                return this.name().compareTo(((Music) o).name());
            } catch (SQLException e) {
                return 0;
            }
        }
        return 0;
    }
}
