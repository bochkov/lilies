package com.sergeybochkov.lilies.model;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

@Entity
@Table(name = "music")
public class Music implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "music_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "subname")
    private String subName;

    @Column(name = "composer")
    @ManyToMany
    private Author composer;

    @Column(name = "writer")
    @ManyToMany
    private Author writer;

    @Column(name = "difficulty")
    @ManyToOne
    private Difficulty difficulty;

    @Column(name = "instrument")
    @ManyToMany
    private Instrument instrument;

    @Column(name = "src_file")
    private File srcFile;

    @Column(name = "pdf_file")
    private File pdfFile;

    @Column(name = "mp3_file")
    private File mp3File;

    public Music() {}

    public Music(String name, String subName, Author composer, Author writer, Difficulty difficulty, Instrument instrument, File pdfFile, File mp3File, File srcFile) {
        this.name = name;
        this.subName = subName;
        this.composer = composer;
        this.writer = writer;
        this.difficulty = difficulty;
        this.instrument = instrument;
        this.pdfFile = pdfFile;
        this.mp3File = mp3File;
        this.srcFile = srcFile;
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

    public Author getComposer() {
        return composer;
    }

    public void setComposer(Author composer) {
        this.composer = composer;
    }

    public Author getWriter() {
        return writer;
    }

    public void setWriter(Author writer) {
        this.writer = writer;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public File getMp3File() {
        return mp3File;
    }

    public void setMp3File(File mp3File) {
        this.mp3File = mp3File;
    }

    public File getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }
}
