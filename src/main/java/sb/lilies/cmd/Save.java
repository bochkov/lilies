package sb.lilies.cmd;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import com.jcabi.log.Logger;
import sb.lilies.Instrument;

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;

public final class Save implements Execute {

    private final Source source;
    private final DataSource ds;
    private final Metadata md;

    public Save(Metadata md, DataSource ds, Source source) {
        this.source = source;
        this.ds = ds;
        this.md = md;
    }

    @Override
    public void act() throws Exception {
        Long storageId = new JdbcSession(ds)
                .sql("INSERT INTO storage (filename, src, pdf, mp3) VALUES (?, ?, ?, ?)")
                .set(source.filename())
                .set(new PgOidIncome(ds, source.ly()).id())
                .set(new PgOidIncome(ds, source.pdf()).id())
                .set(new PgOidIncome(ds, source.mp3()).id())
                .insert(new SingleOutcome<>(Long.class));
        Logger.debug(this, "Создана запись в storage");
        new File(String.format("%s.mp3", source.filename())).delete();
        new File(String.format("%s.wav", source.filename())).delete();
        new File(String.format("%s.pdf", source.filename())).delete();
        new File(String.format("%s.midi", source.filename())).delete();
        Logger.debug(this, "Удалены промежуточные файлы");
        Long musicId = new JdbcSession(ds)
                .sql("INSERT INTO music (name, subname, difficulty, storage_id) VALUES (?, ?, ?, ?)")
                .set(md.title())
                .set(md.subtitle())
                .set(md.difficulty())
                .set(storageId)
                .insert(new SingleOutcome<>(Long.class));
        Logger.debug(this, "Создана запись в music");
        if (md.composer() != null && !md.composer().trim().isEmpty()) {
            Long authorId;
            try {
                authorId = new JdbcSession(ds)
                        .sql("SELECT author_id FROM author WHERE last_name = ?")
                        .set(md.composer())
                        .select(new SingleOutcome<>(Long.class));
                Logger.debug(this, "Найдена сущесвующая запись в author; authorId=%s", authorId);
            } catch (SQLException ex) {
                authorId = new JdbcSession(ds)
                        .sql("INSERT INTO author (last_name) VALUES (?)")
                        .set(md.composer())
                        .insert(new SingleOutcome<>(Long.class));
                Logger.debug(this, "Создана запись в author");
            }
            new JdbcSession(ds)
                    .sql("INSERT INTO music_composer (music_id, composer_id) VALUES (?, ?)")
                    .set(musicId)
                    .set(authorId)
                    .insert(Outcome.VOID);
            Logger.debug(this, "Создана привязка music к composer");
        }
        if (md.writer() != null && !md.writer().trim().isEmpty()) {
            Long authorId;
            try {
                authorId = new JdbcSession(ds)
                        .sql("SELECT author_id FROM author WHERE last_name = ?")
                        .set(md.writer())
                        .select(new SingleOutcome<>(Long.class));
                Logger.debug(this, "Найдена сущесвующая запись в author; authorId=%s", authorId);
            } catch (SQLException ex) {
                authorId = new JdbcSession(ds)
                        .sql("INSERT INTO author (last_name) VALUES (?)")
                        .set(md.writer())
                        .insert(new SingleOutcome<>(Long.class));
                Logger.debug(this, "Создана запись в author");
            }
            new JdbcSession(ds)
                    .sql("INSERT INTO music_writer (music_id, writer_id) VALUES (?, ?)")
                    .set(musicId)
                    .set(authorId)
                    .insert(Outcome.VOID);
            Logger.debug(this, "Создана привязка music к writer");
        }

        for (Instrument i : md.instruments(ds)) {
            new JdbcSession(ds)
                    .sql("INSERT INTO music_instrument VALUES (?, ?)")
                    .set(musicId)
                    .set(i.id())
                    .insert(Outcome.VOID);
            Logger.debug(this, "Создана привязка music к instrument=%s", i.name());
        }
        Logger.info(this, "Создание завершено");
    }
}
