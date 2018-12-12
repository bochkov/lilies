package sb.lilies;

import java.sql.SQLException;
import java.util.List;

public interface Music extends Comparable {

    int id();

    String name() throws SQLException;

    String subname() throws SQLException;

    Iterable<Instrument> instrument() throws SQLException;

    Difficulty difficulty() throws SQLException;

    Iterable<Author> composer() throws SQLException;

    Iterable<Author> writer() throws SQLException;

    Storage storage() throws SQLException;

    boolean withInstruments(List<String> slugs) throws SQLException;

    boolean withDifficulties(List<String> diffs) throws SQLException;
}
