package sb.lilies.cmd;

import sb.lilies.Difficulty;
import sb.lilies.Instrument;
import sb.lilies.PgDifficulty;
import sb.lilies.PgInstruments;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Metadata {

    private final String title;
    private final String subtitle;
    private final String composer;
    private final String writer;
    private final String[] instrument;
    private final int difficulty;

    public Metadata(String title, String subtitle, String composer, String writer, String[] instrument, int difficulty) {
        this.title = title;
        this.subtitle = subtitle.replaceAll("\\\\", "");
        this.composer = composer;
        this.writer = writer;
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
        List<Instrument> insts = new ArrayList<>();
        for (String i : instrument)
            insts.add(new PgInstruments(ds).find(i));
        return insts;
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
        List<String> strInst = instruments(ds).stream().map(inst -> {
            try {
                return String.format("%s (%s)", inst.name(), inst.slug());
            } catch (SQLException e) {
                return "<unknown>";
            }
        }).collect(Collectors.toList());
        String str = String.format("title=%s\nsubtitle=%s\ndifficulty=%s(%s)\ninstruments=%s\ncomposer=%s\nwriter=%s\n\n",
                title, subtitle, diff.rating(), diffName, strInst, composer, writer);
        out.write(str.getBytes());
    }

    @Override
    public String toString() {
        return String.format("Metadata {title='%s', subtitle='%s', composer='%s', instrument=%s, difficulty=%d}",
                title, subtitle, composer, Arrays.toString(instrument), difficulty);
    }
}
