package sb.lilies;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public final class CtStorage implements Storage {

    private final Storage origin;
    private final String filename;

    public CtStorage(Storage origin, String filename) {
        this.origin = origin;
        this.filename = filename;
    }


    @Override
    public String filename() {
        return this.filename;
    }

    @Override
    public String srcFn() {
        return String.format("src/%s.ly", filename());
    }

    @Override
    public byte[] src() throws SQLException {
        return this.origin.src();
    }

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
    public String pdfFn() {
        return String.format("pdf/%s.pdf", filename());
    }

    @Override
    public byte[] pdf() throws SQLException {
        return this.origin.pdf();
    }

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
    public String mp3Fn() {
        return String.format("mp3/%s.mp3", filename());
    }

    @Override
    public byte[] mp3() throws SQLException {
        return this.origin.mp3();
    }

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
}
