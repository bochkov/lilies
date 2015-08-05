package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Instrument;

public interface InstrumentService {

    Instrument getOrSave(Instrument instrument);
}
