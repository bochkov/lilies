package sb.lilies2.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.persistence.*;

import lombok.Data;
import org.apache.commons.io.FileUtils;

@Data
@Entity
@Table(name = "storage")
public final class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "storage_id_seq")
    @Column(name = "storage_id")
    private Long id;

    private String filename;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] src;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] pdf;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] mp3;

    public String srcFn() {
        return String.format("src/%s.ly", filename);
    }

    public boolean hasSrc() throws IOException {
        File outFile = new File("media", srcFn());
        if (!outFile.exists()) {
            FileUtils.forceMkdirParent(outFile);
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                out.write(src);
            }
        }
        return outFile.exists();
    }

    public String pdfFn() {
        return String.format("pdf/%s.pdf", filename);
    }

    public boolean hasPdf() throws IOException {
        File outFile = new File("media", pdfFn());
        if (!outFile.exists()) {
            FileUtils.forceMkdirParent(outFile);
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                out.write(pdf);
            }
        }
        return outFile.exists();
    }

    public String mp3Fn() {
        return String.format("mp3/%s.mp3", filename);
    }

    public boolean hasMp3() throws IOException {
        File outFile = new File("media", mp3Fn());
        if (!outFile.exists()) {
            FileUtils.forceMkdirParent(outFile);
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                out.write(mp3);
            }
        }
        return outFile.exists();
    }

}
