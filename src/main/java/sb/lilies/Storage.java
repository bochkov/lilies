package sb.lilies;

import java.io.IOException;
import java.sql.SQLException;

public interface Storage {

    String filename() throws SQLException;

    String srcFn() throws SQLException;

    boolean hasSrc() throws SQLException, IOException;

    byte[] src() throws SQLException;

    String pdfFn() throws SQLException;

    boolean hasPdf() throws SQLException, IOException;

    byte[] pdf() throws SQLException;

    String mp3Fn() throws SQLException;

    boolean hasMp3() throws SQLException, IOException;

    byte[] mp3() throws SQLException;
}
