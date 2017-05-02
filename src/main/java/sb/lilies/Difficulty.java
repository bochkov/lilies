package sb.lilies;

import java.sql.SQLException;

public interface Difficulty {

    int rating();

    String name() throws SQLException;
}
