package sb.lilies;

import java.sql.SQLException;

public interface Authors {

    Iterable<Author> iterate() throws SQLException;

    Iterable<Author> forSheet(int id) throws SQLException;

}
