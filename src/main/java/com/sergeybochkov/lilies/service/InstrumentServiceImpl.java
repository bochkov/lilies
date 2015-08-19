package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    private InstrumentRepository repo;

    @Override
    public Instrument findBySlug(String slug) {
        return repo.findBySlug(slug);
    }

    @Override
    public Instrument findOne(Long id) {
        return repo.findOne(id);
    }

    @Override
    public List<Instrument> findAll() {
        return repo.findAll(new Sort("name"));
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    @Override
    public Instrument save(Instrument instrument) {
        if (instrument.getId() != null) {
            Instrument i = repo.findOne(instrument.getId());
            i.setName(instrument.getName());
            i.setSlug(instrument.getSlug());
            return repo.save(i);
        }
        else
            return repo.save(instrument);
    }
}
