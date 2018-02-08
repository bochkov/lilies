package sb.lilies;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class CtMusic implements Music, Comparable {

    private final Music origin;
    private final String name;
    private final String subname;
    private final Storage storage;
    private final Difficulty difficulty;
    private final List<Author> composer = new ArrayList<>();
    private final List<Author> writer = new ArrayList<>();
    private final List<Instrument> instruments = new ArrayList<>();

    public CtMusic(Music origin, String name, String subname, Storage storage, Difficulty difficulty) {
        this.origin = origin;
        this.name = name;
        this.subname = subname;
        this.storage = storage;
        this.difficulty = difficulty;
    }

    @Override
    public int id() {
        return this.origin.id();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String subname() {
        return subname;
    }

    @Override
    public Iterable<Instrument> instrument() throws SQLException {
        if (this.instruments.isEmpty()) {
            this.origin.instrument().forEach(this.instruments::add);
        }
        return this.instruments;
    }

    @Override
    public Difficulty difficulty() {
        return this.difficulty;
    }

    @Override
    public Iterable<Author> composer() throws SQLException {
        if (this.composer.isEmpty()) {
            this.origin.composer().forEach(this.composer::add);
        }
        return this.composer;
    }

    @Override
    public Iterable<Author> writer() throws SQLException {
        if (this.writer.isEmpty()) {
            this.origin.writer().forEach(this.writer::add);
        }
        return this.writer;
    }

    @Override
    public Storage storage() {
        return this.storage;
    }

    @Override
    public boolean withInstruments(List<String> slugs) throws SQLException {
        return this.origin.withInstruments(slugs);
    }

    @Override
    public boolean withDifficulties(List<String> diffs) throws SQLException {
        return this.origin.withDifficulties(diffs);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Music) {
            try {
                return this.name().compareTo(((Music) o).name());
            } catch (SQLException e) {
                return 0;
            }
        }
        return 0;
    }
}
