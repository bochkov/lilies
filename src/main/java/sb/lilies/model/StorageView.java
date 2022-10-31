package sb.lilies.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public interface StorageView {

    Long getId();

    String getFilename();

    byte[] getSrc();

    byte[] getPdf();

    byte[] getMp3();

    default String srcFn() {
        return String.format("src/%s.ly", getFilename());
    }

    default boolean hasSrc() throws IOException {
        File outFile = new File("media", srcFn());
        if (!outFile.exists()) {
            FileUtils.forceMkdirParent(outFile);
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                out.write(getSrc());
            }
        }
        return outFile.exists();
    }

    default String pdfFn() {
        return String.format("pdf/%s.pdf", getFilename());
    }

    default boolean hasPdf() throws IOException {
        File outFile = new File("media", pdfFn());
        if (!outFile.exists()) {
            FileUtils.forceMkdirParent(outFile);
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                out.write(getPdf());
            }
        }
        return outFile.exists();
    }

    default String mp3Fn() {
        return String.format("mp3/%s.mp3", getFilename());
    }

    default boolean hasMp3() throws IOException {
        File outFile = new File("media", mp3Fn());
        if (!outFile.exists()) {
            FileUtils.forceMkdirParent(outFile);
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                out.write(getMp3());
            }
        }
        return outFile.exists();
    }

}
