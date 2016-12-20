package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.repository.DifficultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class DifficultyServiceImpl implements DifficultyService {

    private final DifficultyRepository repo;

    @Autowired
    public DifficultyServiceImpl(DifficultyRepository repo) {
        this.repo = repo;
    }

    @Override
    public Difficulty get(Integer id) {
        return repo.findOne(id);
    }

    @Override
    public List<Difficulty> findByRatingIn(Integer[] ratings) {
        return repo.findByRatingIn(ratings);
    }

    @Override
    public List<Difficulty> findAll() {
        return repo.findAll(new Sort("rating"));
    }

    @Override
    public Difficulty findOne(Integer id) {
        return repo.findOne(id);
    }

    @Override
    public Difficulty save(Difficulty difficulty) {
        Difficulty d = repo.findOne(difficulty.getRating());
        return d != null ?
                repo.save(new Difficulty(d.getRating(), d.getName())) :
                repo.save(difficulty);
    }

    @Override
    public void delete(Integer id) {
        repo.delete(id);
    }
}
