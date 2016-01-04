package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.model.Storage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MusicService {

    Music findOne(Long id);

    List<Music> findAll();

    Page<Music> findAll(Integer page);

    List<Music> findByDifficultyAndInstrumentIn(List<Difficulty> difficulties, List<Instrument> instruments);

    List<Music> findBySomething(String name);

    Music save(Music music);

    void generateFiles(Music music);

    void delete(Long id);

    Storage getStorage(Music music);

    Storage save(Storage storage);
}
