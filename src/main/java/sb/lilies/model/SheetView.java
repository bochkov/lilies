package sb.lilies.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class SheetView {

    private final MusicView music;
    private final StorageLink storage;

}
