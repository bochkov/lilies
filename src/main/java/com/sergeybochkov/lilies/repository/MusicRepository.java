package com.sergeybochkov.lilies.repository;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {

    List<Music> findByDifficultyOrInstrumentIn(List<Difficulty> difficulties, List<Instrument> instruments);
}
