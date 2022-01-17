package sb.lilies.cmd;

import java.io.File;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class Lame implements Mp3 {

    private static final String LAME_CMD = "lame -S -f -b 64 %s.wav %s.mp3";

    private final Mp3 origin;

    @Override
    public File mp3() throws IOException {
        File file = this.origin.mp3();
        String fn = new Filename(file.getName()).name();
        String cmd = String.format(LAME_CMD, fn, fn);
        LOG.info("{}: {}", fn, cmd);
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
            LOG.info("{}: Создан {}.mp3", fn, fn);
            return new File(String.format("%s.mp3", fn));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException(ex);
        }
    }
}
