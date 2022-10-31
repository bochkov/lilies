package sb.lilies.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicReference;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Sheet {

    private final File src;
    private final AtomicReference<File> pdf = new AtomicReference<>();
    private final AtomicReference<File> mid = new AtomicReference<>();
    private final AtomicReference<File> wav = new AtomicReference<>();
    private final AtomicReference<File> mp3 = new AtomicReference<>();

    public String filename() {
        return src.getName().substring(0, src.getName().lastIndexOf('.'));
    }

    public File source() {
        return src;
    }

    public File pdf() {
        return this.pdf.get();
    }

    public File mp3() {
        return this.mp3.get();
    }

    public void addPdf(File file) {
        this.pdf.set(file);
    }

    public void addMid(File file) {
        this.mid.set(file);
    }

    public void addWav(File file) {
        this.wav.set(file);
    }

    public void addMp3(File file) {
        this.mp3.set(file);
    }

    public void purgeTemporary() throws IOException {
        if (mid.get() != null) {
            Files.deleteIfExists(mid.get().toPath());
        }
        if (wav.get() != null) {
            Files.deleteIfExists(wav.get().toPath());
        }
    }

}
