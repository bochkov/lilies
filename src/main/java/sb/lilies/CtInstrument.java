package sb.lilies;

import java.sql.SQLException;

public final class CtInstrument implements Instrument {

    private final Instrument origin;
    private final String name;
    private final String slug;

    public CtInstrument(Instrument origin, String name, String slug) {
        this.origin = origin;
        this.name = name;
        this.slug = slug;
    }

    @Override
    public Long id() {
        return this.origin.id();
    }

    @Override
    public String name() throws SQLException {
        return this.name;
    }

    @Override
    public String slug() throws SQLException {
        return this.slug;
    }
}
