package sb.lilies;

import com.jcabi.jdbc.JdbcSession;

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

    public byte[] src() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT src FROM storage WHERE storage_id = ?")
                .set(this.id)
                .select(new PgOidOutcome());
    }

    public byte[] pdf() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT pdf FROM storage WHERE storage_id = ?")
                .set(this.id)
                .select(new PgOidOutcome());
    }

    public byte[] mp3() throws SQLException {
        return new JdbcSession(ds)
                .sql("SELECT mp3 FROM storage WHERE storage_id = ?")
                .set(this.id)
                .select(new PgOidOutcome());
    }

    public void saveSrc(File file) throws SQLException, IOException {
        file.getParentFile().mkdirs();
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(src());
        }
    }

    public void savePdf(File file) throws SQLException, IOException {
        file.getParentFile().mkdirs();
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(pdf());
        }
    }

    public void saveMp3(File file) throws SQLException, IOException {
        file.getParentFile().mkdirs();
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(mp3());
        }
    }
}
