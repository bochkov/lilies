package sb.lilies.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sb.lilies.model.*;
import sb.lilies.repo.DiffRepo;
import sb.lilies.repo.InstrumentRepo;
import sb.lilies.repo.MusicRepo;
import sb.lilies.repo.StorageRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class Musics {

    private final MusicRepo musicRepo;
    private final StorageRepo storageRepo;
    private final InstrumentRepo instRepo;
    private final DiffRepo diffRepo;

    public Long totalSheets() {
        return musicRepo.count();
    }

    public Long totalInstruments() {
        return instRepo.count();
    }

    public List<InstrumentView> allInstruments() {
        return instRepo.findAllProjectedByOrderByName();
    }

    public List<DifficultyView> allRatings() {
        return diffRepo.findAllProjectedByOrderByRating();
    }

    private boolean withDifficulties(MusicView music, List<String> diffs) {
        return !diffs.contains(String.format("%s", music.getDifficulty().getRating()));
    }

    private boolean withInstruments(MusicView m, List<String> slugs) {
        for (InstrumentView instrument : m.getInstruments())
            if (slugs.contains(instrument.getSlug()))
                return false;
        return true;
    }

    public List<MusicView> filter(List<String> difficulties, List<String> instruments) {
        LOG.debug("diffs={}, instruments={}", difficulties, instruments);
        List<MusicView> music = musicRepo.findAllProjectedBy();
        music.removeIf(p -> withDifficulties(p, difficulties));
        music.removeIf(p -> withInstruments(p, instruments));
        LOG.debug("total found={}", music.size());
        return music;
    }

    public MusicView get(Long id) {
        MusicView music = musicRepo.findProjectedById(id);
        StorageView storage = storageRepo.findProjectedById(music.getStorageId());
        return new MusicAllView(music, storage);
    }

    public List<MusicView> search(String query) {
        List<MusicView> all = musicRepo.findAllProjectedBy();
        all.removeIf(p -> !p.matched(query));
        return all;
    }

}
