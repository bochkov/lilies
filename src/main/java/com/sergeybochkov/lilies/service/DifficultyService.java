package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;

import javax.persistence.OrderBy;
import java.util.List;

public interface DifficultyService {

    Difficulty get(Integer id);

    List<Difficulty> findAll();

    Difficulty save(Difficulty difficulty);

    void delete(Integer id);
}
