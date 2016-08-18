package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Instrument;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface InstrumentService {

    Instrument findBySlug(String slug);

    Instrument findOne(Long id);

    List<Instrument> findAll();

    List<Instrument> findAll(Sort sort);

    void delete(Long id);

    Instrument save(Instrument instrument);
}
