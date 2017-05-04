package sb.lilies;

import java.sql.SQLException;

public final class CtWriter implements Author {

    private final Author origin;
    private final String name;

    public CtWriter(Author origin, String name) {
        this.origin = origin;
        this.name = name;
    }

    @Override
    public String name() throws SQLException {
        return name;
    }
}
