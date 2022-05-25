package sb.lilies.app;

import lombok.RequiredArgsConstructor;
import ratpack.core.file.FileHandlerSpec;
import ratpack.func.Action;

@RequiredArgsConstructor
public final class MediaFiles implements Action<FileHandlerSpec> {

    private final boolean develop;

    @Override
    public void execute(FileHandlerSpec fhs) {
        if (develop) {
            fhs.files("media");
        }
    }
}