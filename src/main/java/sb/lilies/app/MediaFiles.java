package sb.lilies.app;

import ratpack.file.FileHandlerSpec;
import ratpack.func.Action;

public final class MediaFiles implements Action<FileHandlerSpec> {

    private final boolean develop;

    public MediaFiles(boolean develop) {
        this.develop = develop;
    }

    @Override
    public void execute(FileHandlerSpec fhs) {
        if (develop) {
            fhs.files("media");
        }
    }
}