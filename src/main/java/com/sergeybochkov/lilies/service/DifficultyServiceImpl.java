package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.repository.DifficultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DifficultyServiceImpl implements DifficultyService {

    @Autowired private DifficultyRepository repo;

    @Override
    public Difficulty get(Integer id) {
        return repo.findOne(id);
    }

    @Override
    public List<Difficulty> findAll() {
        return repo.findAll(new Sort("rating"));
    }

    @Override
    public Difficulty save(Difficulty difficulty) {
        return repo.save(difficulty);
    }

    @Override
    public void delete(Integer id) {
        repo.delete(id);
    }
}
