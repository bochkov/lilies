package com.sergeybochkov.lilies.model;

import org.apache.commons.io.IOUtils;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.*;

@Entity
@Table(name = "storage")
public class Storage implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Storage.class);

    @Id
    @Column(name = "storage_id")
    @GeneratedValue(generator = "storage_id_generator")
    @GenericGenerator(name = "storage_id_generator",strategy = "increment")
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "src_file")
    private byte[] srcFile;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pdf_file")
    private byte[] pdfFile;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "mp3_file")
    private byte[] mp3File;

    public Storage() {
    }

    public Storage(File srcFile) {
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

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public byte[] getSrcFile() {
        return srcFile;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public byte[] getPdfFile() {
        return pdfFile;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public byte[] getMp3File() {
        return mp3File;
    }

    public void exportSrc(File outFile) throws IOException {
        IOUtils.write(srcFile, new FileOutputStream(outFile));
    }

    public void storePdf(File inFile) throws IOException {
        this.pdfFile = IOUtils.toByteArray(new FileInputStream(inFile));
    }

    public void exportPdf(File outFile) throws IOException {
        IOUtils.write(pdfFile, new FileOutputStream(outFile));
    }

    public void storeMp3(File file) throws IOException {
        this.mp3File = IOUtils.toByteArray(new FileInputStream(file));
    }

    public void exportMp3(File outFile) throws IOException {
        IOUtils.write(mp3File, new FileOutputStream(outFile));
    }
}
