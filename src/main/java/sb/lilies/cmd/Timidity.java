package sb.lilies.cmd;

import com.jcabi.log.Logger;

import java.io.File;

public final class Timidity implements Mp3 {

    private static final String TIMIDITY_CMD = "timidity --output-24bit -Ow %s.midi";

    private final Midi midi;

    public Timidity(Midi midi) {
        this.midi = midi;
    }

    @Override
    public File mp3() throws Exception {
        String fn = midi.midi().getName().substring(0, midi.midi().getName().lastIndexOf("."));
        String cmd = String.format(TIMIDITY_CMD, fn);
        Logger.info(this, "%s: %s", fn, cmd);
        Runtime.getRuntime().exec(cmd).waitFor();
        Logger.info(this, "%s: Создан %s.wav", fn, fn);
        return new File(String.format("%s.wav", fn));
    }
}
