package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.model.Storage;

import java.util.List;
import java.util.Map;

public interface MusicService {

    Music findOne(Long id);

    List<Music> findAll();

    List<Music> findByDifficultyAndInstrumentIn(List<Difficulty> difficulties, List<Instrument> instruments);

    List<Music> findByNameContainingIgnoreCase(String name);

    Music save(Music music);

    void generateFiles(Music music);

    void delete(Long id);

    Storage getStorage(Music music);

    Storage save(Storage storage);
}
