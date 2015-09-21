package com.sergeybochkov.lilies.repository;

import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage, Long> {

    //Storage findByMusic(Music music);
}
