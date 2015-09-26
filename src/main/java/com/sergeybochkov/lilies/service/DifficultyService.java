package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;

import java.util.List;

public interface DifficultyService {

    Difficulty get(Integer id);

    List<Difficulty> findAll();

    Difficulty findOne(Integer id);

    Difficulty save(Difficulty difficulty);

    void delete(Integer id);
}
