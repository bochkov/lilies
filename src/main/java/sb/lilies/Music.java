package sb.lilies;

import java.sql.SQLException;
import java.util.List;

public interface Music {

    String name() throws SQLException;

    boolean withInstruments(List<String> slugs) throws SQLException;

    boolean withDifficulties(List<String> diifs) throws SQLException;
}
