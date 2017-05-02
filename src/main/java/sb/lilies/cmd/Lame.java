package sb.lilies.cmd;

import com.jcabi.log.Logger;

import java.io.File;

public final class Lame implements Mp3 {

    private static final String LAME_CMD = "lame -S -f -b 64 %s.wav %s.mp3";

    private final Mp3 origin;

    public Lame(Mp3 origin) {
        this.origin = origin;
    }

    @Override
    public File mp3() throws Exception {
        File file = this.origin.mp3();
        String fn = file.getName().substring(0, file.getName().lastIndexOf("."));
        String cmd = String.format(LAME_CMD, fn, fn);
        Logger.info(this, "%s: %s", fn, cmd);
        Runtime.getRuntime().exec(cmd).waitFor();
        Logger.info(this, "%s: Создан %s.mp3", fn, fn);
        return new File(String.format("%s.mp3", fn));
    }
}
