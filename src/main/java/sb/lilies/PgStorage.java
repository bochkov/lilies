package sb.lilies;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public final class PgStorage implements Storage {

    private final DataSource ds;
    private final int id;

    public PgStorage(DataSource ds, int id) {
        this.ds = ds;
        this.id = id;
    }

    @Override
    public String srcFn() throws SQLException {
        return String.format("src/%s.ly", filename());
    }

    @Override
    public byte[] src() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT src FROM storage WHERE storage_id = ?")
                .set(this.id)
                .select(new PgOidOutcome());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean hasSrc() throws IOException, SQLException {
        File outFile = new File("media", srcFn());
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                out.write(src());
            }
        }
        return outFile.exists();
    }

    @Override
    public String pdfFn() throws SQLException {
        return String.format("pdf/%s.pdf", filename());
    }

    @Override
    public byte[] pdf() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT pdf FROM storage WHERE storage_id = ?")
                .set(this.id)
                .select(new PgOidOutcome());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean hasPdf() throws SQLException, IOException {
        File outFile = new File("media", pdfFn());
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                out.write(pdf());
            }
        }
        return outFile.exists();
    }

    @Override
    public String mp3Fn() throws SQLException {
        return String.format("mp3/%s.mp3", filename());
    }

    @Override
    public byte[] mp3() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT mp3 FROM storage WHERE storage_id = ?")
                .set(this.id)
                .select(new PgOidOutcome());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean hasMp3() throws SQLException, IOException {
        File outFile = new File("media", mp3Fn());
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                out.write(mp3());
            }
        }
        return outFile.exists();
    }

    @Override
    public String filename() throws SQLException {
        return new JdbcSession(ds)
                        .sql("SELECT filename FROM storage WHERE storage_id = ?")
                        .set(id)
                        .select(new SingleOutcome<>(String.class));
    }
}
