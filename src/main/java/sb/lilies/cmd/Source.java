package sb.lilies.cmd;

import java.io.File;
import java.io.IOException;

public interface Source {

    String filename();

    File ly();

    File pdf() throws IOException;

    File mp3() throws IOException;

}
