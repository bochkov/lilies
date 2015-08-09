package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Instrument;

import java.util.List;

public interface InstrumentService {

    Instrument getOrSave(Instrument instrument);

    List<Instrument> findAll();

    void delete(Long id);
}
