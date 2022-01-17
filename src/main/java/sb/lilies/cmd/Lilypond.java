package sb.lilies.cmd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Lilypond implements Pdf, Midi {

    private static final String LY_CMD = "lilypond -dno-point-and-click %s";

    private final String fn;
    private final File file;
    private final List<File> midis = new ArrayList<>();
    private final List<File> pdfs = new ArrayList<>();

    public Lilypond(File file) {
        this.file = file;
        this.fn = file.getName().substring(0, file.getName().lastIndexOf("."));
    }

    @Override
    public File pdf() throws IOException {
        if (pdfs.isEmpty())
            generate();
        return pdfs.get(0);
    }

    @Override
    public File midi() throws IOException {
        if (midis.isEmpty())
            generate();
        return midis.get(0);
    }

    public void generate() throws IOException {
        String cmd = String.format(LY_CMD, file.getCanonicalPath());
        LOG.info("{}: {}", fn, cmd);
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
            pdfs.add(new File(String.format("%s.pdf", fn)));
            midis.add(new File(String.format("%s.midi", fn)));
            LOG.info("{}: Созданы {}.pdf и {}.midi", fn, fn, fn);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException(ex);
        }
    }
}
