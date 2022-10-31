package sb.lilies.service.convert;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sb.lilies.model.Sheet;

@Slf4j
@Service
public final class Timidity implements Convert {

    private static final String TIMIDITY_CMD = "timidity --output-24bit -Ow %s.midi";

    @Override
    public void convert(Sheet sheet) throws IOException {
        String fn = sheet.filename();
        String cmd = String.format(TIMIDITY_CMD, fn);
        LOG.info("{}: {}", fn, cmd);
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
            LOG.info("{}: created {}.wav", fn, fn);
            sheet.addWav(new File(String.format("%s.wav", fn)));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException(ex);
        }
    }
}
