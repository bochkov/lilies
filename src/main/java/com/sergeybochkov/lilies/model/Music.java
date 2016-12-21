package com.sergeybochkov.lilies.model;

import com.sergeybochkov.lilies.config.StaticResourceConfig;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "music")
public final class Music implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Music.class);

    @Id
    @Column(name = "music_id")
    @GeneratedValue(generator = "music_seq_generator")
    @GenericGenerator(name = "music_seq_generator", strategy = "increment")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "subname")
    private String subname;

    @ManyToMany
    @JoinTable(name = "music_composer",
            joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "composer_id"))
    private List<Author> composer;

    @ManyToMany
    @JoinTable(name = "music_writer",
            joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id"))
    private List<Author> writer;

    @ManyToOne
    @JoinColumn(name = "difficulty")
    private Difficulty difficulty;

    @ManyToMany
    @JoinTable(name = "music_instrument",
            joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "instrument_id"))
    private List<Instrument> instrument;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @Column(name = "base_filename")
    private String baseFilename;

    @Column(name = "src_filename")
    private String srcFilename;

    @Column(name = "src_length")
    private Long srcFileLength;

    @Column(name = "pdf_filename")
    private String pdfFilename;

    @Column(name = "pdf_length")
    private Long pdfFileLength;

    @Column(name = "mp3_filename")
    private String mp3Filename;

    @Column(name = "mp3_length")
    private Long mp3FileLength;

    public Music() {
    }

    public Music(Long id, String name, String subname, List<Author> composer, List<Author> writer,
                 Difficulty difficulty, List<Instrument> instruments, Music music) {
        this(id, name, subname, composer, writer, difficulty, instruments, (File) null);
        this.baseFilename = music.getBaseFilename();
        this.srcFilename = music.getSrcFilename();
        this.srcFileLength = music.getSrcFileLength();
        this.pdfFilename = music.getPdfFilename();
        this.pdfFileLength = music.getPdfFileLength();
        this.mp3Filename = music.getMp3Filename();
        this.mp3FileLength = music.getMp3FileLength();
        this.storage = music.getStorage();
    }

    public Music(Long id, String name, String subname, List<Author> composer, List<Author> writer,
                 Difficulty difficulty, List<Instrument> instrument, File srcFile) {
        this.id = id;
        this.name = name;
        this.subname = subname;
        this.composer = new ArrayList<>(composer);
        this.writer = new ArrayList<>(writer);
        this.difficulty = difficulty;
        this.instrument = new ArrayList<>(instrument);
        if (srcFile != null) {
            this.baseFilename = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
            this.storage = new Storage(srcFile);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubname() {
        return subname;
    }

    public List<Author> getComposer() {
        return composer;
    }

    public List<Author> getWriter() {
        return writer;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<Instrument> getInstrument() {
        return instrument;
    }

    public String getBaseFilename() {
        return baseFilename;
    }

    public String getSrcFilename() {
        return srcFilename;
    }

    public Long getSrcFileLength() {
        return srcFileLength;
    }

    public String getPdfFilename() {
        return pdfFilename;
    }

    public Long getPdfFileLength() {
        return pdfFileLength;
    }

    public String getMp3Filename() {
        return mp3Filename;
    }

    public Long getMp3FileLength() {
        return mp3FileLength;
    }

    public Storage getStorage() {
        return storage;
    }

    public boolean hasPdf() {
        return pdfFilename != null
                && pdfFileLength != null
                && pdfFileLength > 0;
    }

    public boolean hasSrc() {
        return srcFilename != null
                && srcFileLength != null
                && srcFileLength > 0;
    }

    public boolean hasMp3() {
        return mp3Filename != null
                && mp3FileLength != null
                && mp3FileLength > 0;
    }

    public void createFiles() {
        try {
            if (hasSrc())
                storage.exportSrc(new File(StaticResourceConfig.MEDIA_DIR, srcFilename));
        }
        catch (IOException ex) {
            LOG.warn("Cannot export " + srcFilename);
        }
        try {
            if (hasPdf())
                storage.exportPdf(new File(StaticResourceConfig.MEDIA_DIR, pdfFilename));
        }
        catch (IOException ex) {
            LOG.warn("Cannot export " + pdfFilename);
        }
        try {
            if (hasMp3())
                storage.exportMp3(new File(StaticResourceConfig.MEDIA_DIR, mp3Filename));
        }
        catch (IOException ex) {
            LOG.warn("Cannot export " + mp3Filename);
        }
    }

    public void updateSrc(File file) throws IOException {
        this.storage.exportSrc(file);
        this.srcFilename = "src/" + file.getName();
        this.srcFileLength = file.length();
    }

    public void updatePdf(File file) throws IOException {
        this.pdfFilename = "pdf/" + file.getName();
        this.pdfFileLength = file.length();
    }

    public void updateMp3(File file) {
        this.mp3Filename = "mp3/" + file.getName();
        this.mp3FileLength = file.length();
    }

    public void deleteFiles() {
        if (hasSrc() && !new File(StaticResourceConfig.MEDIA_DIR, srcFilename).delete())
            LOG.warn("Cannot delete " + srcFilename);
        if (hasPdf() && !new File(StaticResourceConfig.MEDIA_DIR, pdfFilename).delete())
            LOG.warn("Cannot delete " + pdfFilename);
        if (hasMp3() && !new File(StaticResourceConfig.MEDIA_DIR, mp3Filename).delete())
            LOG.warn("Cannot delete " + mp3Filename);
    }
}
