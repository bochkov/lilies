package sb.lilies.service;

import java.io.File;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sb.lilies.model.Sheet;
import sb.lilies.service.convert.Convert;

@Service
@RequiredArgsConstructor
public final class LyConvert {

    private final Convert lilypond;
    private final Convert timidity;
    private final Convert lame;

    public Sheet generate(File file) throws IOException {
        Sheet sheet = new Sheet(file);
        lilypond.convert(sheet);
        timidity.convert(sheet);
        lame.convert(sheet);
        sheet.purgeTemporary();
        return sheet;
    }

}
