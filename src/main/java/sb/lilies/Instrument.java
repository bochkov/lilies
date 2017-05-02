package sb.lilies;

import java.sql.SQLException;

public interface Instrument {

    Long id();

    String name() throws SQLException;

    String slug() throws SQLException;

}
