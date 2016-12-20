package com.sergeybochkov.lilies.repository;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {

    @Transactional
    @Query("SELECT DISTINCT m FROM Music m " +
            "LEFT OUTER JOIN m.instrument i " +
            "WHERE m.difficulty IN(?1) AND i IN(?2)")
    List<Music> findByDifficultyAndInstrumentIn(List<Difficulty> difficulties, List<Instrument> instruments);

    @Transactional
    @Query("SELECT m FROM Music m " +
            "LEFT JOIN m.composer c " +
            "LEFT JOIN m.writer w " +
            "WHERE UPPER(m.name) LIKE UPPER(?1) " +
            "OR UPPER(m.subname) LIKE UPPER(?1) " +
            "OR UPPER(c.lastname) LIKE UPPER(?1) " +
            "OR UPPER(w.lastname) LIKE UPPER(?1)")
    List<Music> findBySomething(String something);
}
