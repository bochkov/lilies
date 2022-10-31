package sb.lilies.service.convert;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sb.lilies.model.Sheet;

@Slf4j
@Service
public final class Lilypond implements Convert {

    private static final String LY_CMD = "lilypond -dno-point-and-click %s";

    @Override
    public void convert(Sheet sheet) throws IOException {
        String cmd = String.format(LY_CMD, sheet.source().getCanonicalPath());
        String fn = sheet.filename();
        LOG.info("{}: {}", fn, cmd);
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
            LOG.info("{}: Созданы {}.pdf и {}.midi", fn, fn, fn);
            sheet.addPdf(new File(String.format("%s.pdf", fn)));
            sheet.addMid(new File(String.format("%s.midi", fn)));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException(ex);
        }
    }
}
