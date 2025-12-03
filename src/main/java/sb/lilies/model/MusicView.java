package sb.lilies.model;

import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public interface MusicView extends Comparable<MusicView> {

    Long getId();

    String getName();

    String getSubname();

    DifficultyView getDifficulty();

    Set<InstrumentView> getInstruments();

    Set<AuthorView> getComposers();

    Set<AuthorView> getWriters();

    @Value("#{target.storage}")
    Long getStorageId();

    @Override
    default int compareTo(MusicView o) {
        return getName().compareTo(o.getName());
    }

}
