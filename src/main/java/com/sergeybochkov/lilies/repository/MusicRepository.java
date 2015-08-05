package com.sergeybochkov.lilies.repository;

import com.sergeybochkov.lilies.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
