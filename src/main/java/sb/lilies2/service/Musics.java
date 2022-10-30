package sb.lilies2.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sb.lilies2.model.Difficulty;
import sb.lilies2.model.Instrument;
import sb.lilies2.model.Music;
import sb.lilies2.repo.DiffRepo;
import sb.lilies2.repo.InstrumentRepo;
import sb.lilies2.repo.MusicRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class Musics {

    private final MusicRepo musicRepo;
    private final InstrumentRepo instRepo;
    private final DiffRepo diffRepo;

    public Long totalSheets() {
        return musicRepo.count();
    }

    public Long totalInstruments() {
        return instRepo.count();
    }

    public List<Instrument> allInstruments() {
        return instRepo.findAll(Sort.by("name"));
    }

    public List<Difficulty> allRatings() {
        return diffRepo.findAll(Sort.by("rating"));
    }

    private boolean withDifficulties(Music music, List<String> diffs) {
        return !diffs.contains(String.format("%s", music.getDifficulty().getRating()));
    }

    private boolean withInstruments(Music m, List<String> slugs) {
        for (Instrument instrument : m.getInstruments())
            if (slugs.contains(instrument.getSlug()))
                return false;
        return true;
    }

    public List<Music> filter(List<String> difficulties, List<String> instruments) {
        LOG.info("diffs={}, instruments={}", difficulties, instruments);
        List<Music> music = musicRepo.findAll();
        music.removeIf(p -> withDifficulties(p, difficulties));
        music.removeIf(p -> withInstruments(p, instruments));
        LOG.info("total found={}", music.size());
        return music;
    }

    public Music get(Long id) {
        return musicRepo.findById(id)
                .orElseThrow();
    }

    public List<Music> search(String query) {
        List<Music> all = musicRepo.findAll();
        all.removeIf(p -> !p.matched(query));
        return all;
    }

}
