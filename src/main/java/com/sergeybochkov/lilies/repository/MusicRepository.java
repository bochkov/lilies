package com.sergeybochkov.lilies.repository;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {

    List<Music> findByDifficultyIn(List<Difficulty> difficulties);

    List<Music> findByInstrumentIn(List<Instrument> instruments);

    @Query("select m from Music m LEFT JOIN m.composer c LEFT JOIN m.writer w WHERE " +
            "upper(m.name) like upper(?1) or " +
            "upper(m.subName) like upper(?1) or " +
            "upper(c.lastName) like upper(?1) or " +
            "upper(w.lastName) like upper(?1)")
    List<Music> findBySomething(String something);
}
