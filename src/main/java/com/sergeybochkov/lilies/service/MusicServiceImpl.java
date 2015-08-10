package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicRepository repo;

    @Override
    public Music findOne(Long id) {
        return repo.findOne(id);
    }

    @Override
    public List<Music> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Music> findByDifficultyOrInstrumentIn(List<Difficulty> difficulties, List<Instrument> instruments) {
        return repo.findByDifficultyOrInstrumentIn(difficulties, instruments);
    }

    @Override
    public Music save(Music music) {
        return repo.save(music);
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }
}
