package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;

import java.util.List;

public interface MusicService {

    Music findOne(Long id);

    List<Music> findAll();

    List<Music> findByDifficultyOrInstrumentIn(List<Difficulty> difficulties, List<Instrument> instruments);

    Music save(Music music);

    void delete(Long id);
}
