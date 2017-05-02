package sb.lilies.cmd;

import java.io.File;

public interface Source {

    String filename();

    File ly();

    File pdf() throws Exception;

    File mp3() throws Exception;

}
