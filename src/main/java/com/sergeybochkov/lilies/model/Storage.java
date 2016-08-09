package com.sergeybochkov.lilies.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "storage")
public class Storage implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storage_seq_generator")
    @SequenceGenerator(name = "storage_seq_generator", sequenceName = "storage_sequence")
    private Long id;

    @OneToOne(mappedBy = "storage")
    private Music music;

    @Column(name = "src_file")
    private byte[] srcFile;

    @Column(name = "pdf_file")
    private byte[] pdfFile;

    @Column(name = "mp3_file")
    private byte[] mp3File;

    public Storage() {}

    public Storage(Music music) {
        this.music = music;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
