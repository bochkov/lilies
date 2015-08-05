package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    private InstrumentRepository repo;

    @Override
    public Instrument getOrSave(Instrument instrument) {
        Instrument in = repo.findByName(instrument.getName());
        return in != null ? in : repo.save(instrument);
    }
}
