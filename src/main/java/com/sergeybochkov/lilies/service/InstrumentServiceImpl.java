package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository repo;

    @Autowired
    public InstrumentServiceImpl(InstrumentRepository repo) {
        this.repo = repo;
    }

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
        return findAll(new Sort("name"));
    }

    @Override
    public List<Instrument> findAll(Sort sort) {
        return repo.findAll(sort);
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    @Override
    public Instrument save(Instrument instrument) {
        return repo.save(instrument);
    }
}
