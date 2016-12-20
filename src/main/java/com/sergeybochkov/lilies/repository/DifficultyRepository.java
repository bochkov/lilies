package com.sergeybochkov.lilies.repository;

import com.sergeybochkov.lilies.model.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DifficultyRepository extends JpaRepository<Difficulty, Integer> {

    List<Difficulty> findByRatingIn(Integer[] ratings);
}
