package com.sergeybochkov.lilies.model;

import javax.persistence.*;
import java.io.File;
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
    private List<Author> composer;

    @ManyToMany
    private List<Author> writer;

    @ManyToOne
    @JoinColumn(name = "difficulty")
    private Difficulty difficulty;

    @ManyToMany
    private List<Instrument> instrument;

    @Column(name = "src_file")
    private byte[] srcFile;

    @Column(name = "pdf_file")
    private byte[] pdfFile;

    @Column(name = "mp3_file")
    private byte[] mp3File;

    public Music() {}

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

    public byte[] getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(byte[] srcFile) {
        this.srcFile = srcFile;
    }

    public byte[] getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(byte[] pdfFile) {
        this.pdfFile = pdfFile;
    }

    public byte[] getMp3File() {
        return mp3File;
    }

    public void setMp3File(byte[] mp3File) {
        this.mp3File = mp3File;
    }

    public boolean hasSrc() {
        return srcFile != null && srcFile.length > 0;
    }

    public boolean hasPdf() {
        return pdfFile != null && pdfFile.length > 0;
    }

    public boolean hasMp3() {
        return mp3File != null && mp3File.length > 0;
    }
}
