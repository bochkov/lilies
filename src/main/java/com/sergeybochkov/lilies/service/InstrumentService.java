package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Instrument;

import java.util.List;

public interface InstrumentService {

    Instrument findBySlug(String slug);

    Instrument findOne(Long id);

    List<Instrument> findAll();

    void delete(Long id);

    Instrument save(Instrument instrument);
}
