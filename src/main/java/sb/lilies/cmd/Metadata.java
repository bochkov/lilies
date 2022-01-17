package sb.lilies.cmd;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import lombok.ToString;
import sb.lilies.CtInstruments;
import sb.lilies.Difficulty;
import sb.lilies.Instrument;
import sb.lilies.PgDifficulty;

@ToString
public final class Metadata {

    private final String title;
    private final String subtitle;
    private final String composer;
    private final String writer;
    private final String[] instrument;
    private final int difficulty;

    public Metadata(Map<String, String> headers, String[] instrument, int difficulty) {
        this.title = headers.getOrDefault("title", "");
        this.subtitle = headers.getOrDefault("subtitle", "").replace("\\", "");
        this.composer = headers.getOrDefault("composer", "");
        this.writer = headers.getOrDefault("writer", "");
        this.instrument = instrument;
        this.difficulty = difficulty;
    }

    public String title() {
        return title;
    }

    public String subtitle() {
        return subtitle;
    }

    public int difficulty() {
        return difficulty;
    }

    public List<Instrument> instruments(DataSource ds) throws SQLException {
        List<Instrument> instruments = new ArrayList<>();
        for (String i : instrument)
            instruments.add(new CtInstruments(ds).find(i));
        return instruments;
    }

    public String composer() {
        return composer;
    }

    public String writer() {
        return writer;
    }

    public void print(DataSource ds, OutputStream out) throws SQLException, IOException {
        Difficulty diff = new PgDifficulty(ds, difficulty);
        String diffName = diff.name();
        List<String> strInst = instruments(ds).stream()
                .map(inst -> {
                    try {
                        return String.format("%s (%s)", inst.name(), inst.slug());
                    } catch (SQLException e) {
                        return "<unknown>";
                    }
                }).toList();
        String str = String.format("title=%s%nsubtitle=%s%ndifficulty=%s(%s)%ninstruments=%s%ncomposer=%s%nwriter=%s%n%n",
                title, subtitle, diff.rating(), diffName, strInst, composer, writer);
        out.write(str.getBytes());
    }
}
