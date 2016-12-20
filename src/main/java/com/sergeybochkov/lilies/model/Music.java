package com.sergeybochkov.lilies.model;

import org.apache.commons.io.IOUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
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

    @Column(name = "base_filename")
    private String baseFilename;

    @Column(name = "src_filename")
    private String srcFilename;

    @Column(name = "src_length")
    private Long srcFileLength;

    @Lob
    @Column(name = "src_file")
    private byte[] srcFile;

    @Column(name = "pdf_filename")
    private String pdfFilename;

    @Column(name = "pdf_length")
    private Long pdfFileLength;

    @Lob
    @Column(name = "pdf_file")
    private byte[] pdfFile;

    @Column(name = "mp3_filename")
    private String mp3Filename;

    @Column(name = "mp3_length")
    private Long mp3FileLength;

    @Lob
    @Column(name = "mp3_file")
    private byte[] mp3File;

    public Music() {
    }

    public Music(String name, String subname, List<Author> composer, List<Author> writer,
                 Difficulty difficulty, List<Instrument> instrument, File srcFile) {
        this.name = name;
        this.subname = subname;
        this.composer = new ArrayList<>(composer);
        this.writer = new ArrayList<>(writer);
        this.difficulty = difficulty;
        this.instrument = new ArrayList<>(instrument);
        this.baseFilename = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
        if (srcFile.exists()) {
            try {
                this.srcFile = IOUtils.toByteArray(new FileInputStream(srcFile));
            } catch (IOException ex) {
                LOG.warn(ex.getMessage(), ex);
            }
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

    public byte[] getSrcFile() {
        return srcFile;
    }

    public String getPdfFilename() {
        return pdfFilename;
    }

    public Long getPdfFileLength() {
        return pdfFileLength;
    }

    public byte[] getPdfFile() {
        return pdfFile;
    }

    public String getMp3Filename() {
        return mp3Filename;
    }

    public Long getMp3FileLength() {
        return mp3FileLength;
    }

    public byte[] getMp3File() {
        return mp3File;
    }

    public void updateSrc(File file) {
        this.srcFilename = "src/" + file.getName();
        this.srcFileLength = file.length();
    }

    public void updatePdf(File file) {
        this.pdfFilename = "pdf/" + file.getName();
        this.pdfFileLength = file.length();
        try {
            this.pdfFile = IOUtils.toByteArray(new FileInputStream(file));
        }
        catch (IOException ex) {
            LOG.warn(String.format("%s not found, pdf file not stored", file.getName()), ex);
        }
    }

    public void updateMp3(File file) {
        this.mp3Filename = "mp3/" + file.getName();
        this.mp3FileLength = file.length();
        try {
            this.mp3File = IOUtils.toByteArray(new FileInputStream(file));
        }
        catch (IOException ex) {
            LOG.warn(String.format("%s not found, mp3 file not stored", file.getName()), ex);
        }
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
}
