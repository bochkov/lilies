package com.sergeybochkov.lilies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "music")
public class Music implements Serializable {

    @Id
    @GeneratedValue(generator = "music_seq_generator")
    @Column(name = "music_id")
    @SequenceGenerator(name = "music_seq_generator", sequenceName = "music_sequence")
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

    @Column(name = "src_filename")
    private String srcFilename;

    @Column(name = "pdf_filename")
    private String pdfFilename;

    @Column(name = "mp3_filename")
    private String mp3Filename;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "storage_id")
    @JsonIgnore
    private Storage storage;

    public Music() {
    }

    public Music(String name, String subName) {
        this.name = name;
        this.subName = subName;
    }

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

    public String getSrcFilename() {
        return srcFilename;
    }

    public void setSrcFilename(String srcFilename) {
        this.srcFilename = srcFilename;
    }

    public String getPdfFilename() {
        return pdfFilename;
    }

    public void setPdfFilename(String pdfFilename) {
        this.pdfFilename = pdfFilename;
    }

    public String getMp3Filename() {
        return mp3Filename;
    }

    public void setMp3Filename(String mp3Filename) {
        this.mp3Filename = mp3Filename;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
