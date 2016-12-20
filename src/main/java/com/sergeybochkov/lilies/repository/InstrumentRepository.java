package com.sergeybochkov.lilies.repository;

import com.sergeybochkov.lilies.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    Instrument findBySlug(String slug);

    List<Instrument> findBySlugIn(String[] slugs);
}
