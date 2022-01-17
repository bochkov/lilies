package sb.lilies.cmd;

import java.io.File;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class Timidity implements Mp3 {

    private static final String TIMIDITY_CMD = "timidity --output-24bit -Ow %s.midi";

    private final Midi midi;

    @Override
    public File mp3() throws IOException {
        String fn = new Filename(midi.midi().getName()).name();
        String cmd = String.format(TIMIDITY_CMD, fn);
        LOG.info("{}: {}", fn, cmd);
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
            LOG.info("{}: Создан {}.wav", fn, fn);
            return new File(String.format("%s.wav", fn));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException(ex);
        }
    }
}
