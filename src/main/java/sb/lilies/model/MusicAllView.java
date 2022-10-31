package sb.lilies.model;

import java.util.Set;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class MusicAllView implements MusicView {

    private final MusicView mv;
    private final StorageView sv;

    @Override
    public Long getId() {
        return mv.getId();
    }

    @Override
    public String getName() {
        return mv.getName();
    }

    @Override
    public String getSubname() {
        return mv.getSubname();
    }

    @Override
    public DifficultyView getDifficulty() {
        return mv.getDifficulty();
    }

    @Override
    public Set<InstrumentView> getInstruments() {
        return mv.getInstruments();
    }

    @Override
    public Set<AuthorView> getComposers() {
        return mv.getComposers();
    }

    @Override
    public Set<AuthorView> getWriters() {
        return mv.getWriters();
    }

    @Override
    public Long getStorageId() {
        return mv.getStorageId();
    }

    public StorageView getStorage() {
        return sv;
    }
}
