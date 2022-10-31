package sb.lilies.service.convert;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sb.lilies.model.Sheet;

@Slf4j
@Service
public final class Lame implements Convert {

    private static final String CMD = "lame -S -f -b 64 %s.wav %s.mp3";

    @Override
    public void convert(Sheet sheet) throws IOException {
        String fn = sheet.filename();
        String cmd = String.format(CMD, fn, fn);
        LOG.info("{}: {}", fn, cmd);
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
            LOG.info("{}: created {}.mp3", fn, fn);
            sheet.addMp3(new File(String.format("%s.mp3", fn)));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException(ex);
        }
    }

}
