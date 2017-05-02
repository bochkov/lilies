package sb.lilies;

import java.sql.SQLException;

public interface Instruments {

    long count() throws SQLException;

    Instrument find(String slug) throws SQLException;

    Iterable<Instrument> iterate() throws SQLException;

    Iterable<Instrument> forSheet(int id) throws SQLException;
}
