package sb.lilies.model;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

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

    default boolean matched(String query) {
        String token = query.toLowerCase();
        return getName().toLowerCase().contains(token) ||
                getSubname().toLowerCase().contains(token) ||
                getComposers().stream().anyMatch(p -> p.getName().toLowerCase().contains(token)) ||
                getWriters().stream().anyMatch(p -> p.getName().toLowerCase().contains(token));
    }
}
