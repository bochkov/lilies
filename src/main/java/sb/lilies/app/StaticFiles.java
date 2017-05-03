package sb.lilies.app;

import ratpack.file.FileHandlerSpec;
import ratpack.func.Action;

public final class StaticFiles implements Action<FileHandlerSpec> {

    private final boolean develop;

    public StaticFiles(boolean develop) {
        this.develop = develop;
    }

    @Override
    public void execute(FileHandlerSpec fhs) throws Exception {
        if (develop) {
            fhs.path("static").dir("src/main/resources/static");
        } else {
            fhs.files("static");
        }
    }
}