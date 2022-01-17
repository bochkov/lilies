package sb.lilies.app;

import lombok.RequiredArgsConstructor;
import ratpack.file.FileHandlerSpec;
import ratpack.func.Action;

@RequiredArgsConstructor
public final class StaticFiles implements Action<FileHandlerSpec> {

    private final boolean develop;

    @Override
    public void execute(FileHandlerSpec fhs) {
        if (develop) {
            fhs.path("static").dir("src/main/resources/static");
        } else {
            fhs.files("static");
        }
    }
}