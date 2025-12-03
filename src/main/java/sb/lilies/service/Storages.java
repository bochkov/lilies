package sb.lilies.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Supplier;

@Slf4j
public final class Storages {

    public static boolean ensureBytes(File f, Supplier<byte[]> bytes) {
        try {
            FileUtils.forceMkdirParent(f);
            try (FileOutputStream out = new FileOutputStream(f)) {
                out.write(bytes.get());
            }
        } catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
        return f.exists();
    }
}
