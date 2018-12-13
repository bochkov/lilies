package sb.lilies;

import java.sql.SQLException;

public interface Difficulties {

    int maxValue() throws SQLException;

    Iterable<Difficulty> iterate() throws SQLException;

}
