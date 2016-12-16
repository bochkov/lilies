package com.sergeybochkov.lilies.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "music")
public class Music implements Serializable {

    @Id
    @Column(name = "music_id")
    @GeneratedValue(generator = "music_seq_generator")
    @GenericGenerator(name = "music_seq_generator", strategy = "increment")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "subname")
    private String subName;

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
    @JoinTable(name = "difficulty_music",
            joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "rating"))
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public List<Author> getComposer() {
        return composer;
    }

    public void setComposer(List<Author> composer) {
        this.composer = composer;
    }

    public List<Author> getWriter() {
        return writer;
    }

    public void setWriter(List<Author> writer) {
        this.writer = writer;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Instrument> getInstrument() {
        return instrument;
    }

    public void setInstrument(List<Instrument> instrument) {
        this.instrument = instrument;
    }

    public String getBaseFilename() {
        return baseFilename;
    }

    public void setBaseFilename(String baseFilename) {
        this.baseFilename = baseFilename;
    }

    public String getSrcFilename() {
        return srcFilename;
    }

    public void setSrcFilename(String srcFilename) {
        this.srcFilename = srcFilename;
    }

    public Long getSrcFileLength() {
        return srcFileLength;
    }

    public void setSrcFileLength(Long srcFileLength) {
        this.srcFileLength = srcFileLength;
    }

    public byte[] getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(byte[] srcFile) {
        this.srcFile = srcFile;
    }

    public String getPdfFilename() {
        return pdfFilename;
    }

    public void setPdfFilename(String pdfFilename) {
        this.pdfFilename = pdfFilename;
    }

    public Long getPdfFileLength() {
        return pdfFileLength;
    }

    public void setPdfFileLength(Long pdfFileLength) {
        this.pdfFileLength = pdfFileLength;
    }

    public byte[] getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(byte[] pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getMp3Filename() {
        return mp3Filename;
    }

    public void setMp3Filename(String mp3Filename) {
        this.mp3Filename = mp3Filename;
    }

    public Long getMp3FileLength() {
        return mp3FileLength;
    }

    public void setMp3FileLength(Long mp3FileLength) {
        this.mp3FileLength = mp3FileLength;
    }

    public byte[] getMp3File() {
        return mp3File;
    }

    public void setMp3File(byte[] mp3File) {
        this.mp3File = mp3File;
    }

    public boolean hasPdf() {
        return getPdfFilename() != null && getPdfFileLength() != null && getPdfFileLength() > 0;
    }

    public boolean hasSrc() {
        return getSrcFilename() != null && getSrcFileLength() != null && getSrcFileLength() > 0;
    }

    public boolean hasMp3() {
        return getMp3Filename() != null && getMp3FileLength() != null && getMp3FileLength() > 0;
    }
}
