package sb.lilies.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sb.lilies.model.*;
import sb.lilies.repo.DiffRepo;
import sb.lilies.repo.InstrumentRepo;
import sb.lilies.repo.MusicRepo;
import sb.lilies.repo.StorageRepo;

import java.util.List;

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
        Sort sort = Sort.sort(InstrumentView.class)
                .by(InstrumentView::getName)
                .ascending();
        return instRepo.fetchAll(sort);
    }

    public List<DifficultyView> allRatings() {
        Sort sort = Sort.sort(DifficultyView.class)
                .by(DifficultyView::getRating)
                .ascending();
        return diffRepo.fetchAll(sort);
    }

    public List<MusicView> filter(List<Integer> difficulties, List<String> instruments) {
        LOG.debug("diffs={}, instruments={}", difficulties, instruments);
        List<MusicView> music = musicRepo.fetchDiffsAndInstruments(difficulties, instruments);
        LOG.debug("total found={}", music.size());
        return music;
    }

    public SheetView get(Long id) {
        MusicView music = musicRepo.fetch(id);
        if (music == null)
            throw new EntityNotFoundException("Sheet with id=" + id + " not found");
        StorageView storage = storageRepo.fetch(music.getStorageId());
        return storage == null
                ? new SheetView(music, null)
                : new SheetView(music, new StorageLink(storage));
    }

    public List<MusicView> search(String query) {
        return musicRepo.matched(query.toLowerCase());
    }

}
