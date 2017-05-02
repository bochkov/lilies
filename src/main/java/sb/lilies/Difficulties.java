package sb.lilies;

import java.sql.SQLException;

public interface Difficulties {

    long count() throws SQLException;

    Iterable<Difficulty> iterate() throws SQLException;

}
