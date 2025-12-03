package sb.lilies.model;

import sb.lilies.service.Storages;

import java.io.File;

public interface StorageView {

    Long getId();

    String getFilename();

    byte[] getSrc();

    byte[] getPdf();

    byte[] getMp3();

    default String srcFn() {
        return String.format("src/%s.ly", getFilename());
    }

    default boolean hasSrc() {
        File src = new File("media", srcFn());
        return src.exists() || Storages.ensureBytes(src, this::getSrc);
    }

    default String pdfFn() {
        return String.format("pdf/%s.pdf", getFilename());
    }

    default boolean hasPdf() {
        File pdf = new File("media", pdfFn());
        return pdf.exists() || Storages.ensureBytes(pdf, this::getPdf);
    }

    default String mp3Fn() {
        return String.format("mp3/%s.mp3", getFilename());
    }

    default boolean hasMp3() {
        File mp3 = new File("media", mp3Fn());
        return mp3.exists() || Storages.ensureBytes(mp3, this::getMp3);
    }

}
