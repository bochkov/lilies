package sb.lilies.cmd;

import java.io.File;
import java.io.IOException;

public final class Src implements Source {

    private final File ly;
    private final Lilypond lilypond;

    public Src(File ly) {
        this.ly = ly;
        this.lilypond = new Lilypond(ly);
    }

    @Override
    public String filename() {
        return ly.getName()
                .substring(0, ly.getName().lastIndexOf("."))
                .replace("'", "");
    }

    @Override
    public File ly() {
        return ly;
    }

    @Override
    public File pdf() throws IOException {
        return lilypond.pdf();
    }

    @Override
    public File mp3() throws IOException {
        return new Lame(new Timidity(lilypond)).mp3();
    }
}
