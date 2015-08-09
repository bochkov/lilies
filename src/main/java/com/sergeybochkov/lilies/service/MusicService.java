package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Music;

import java.util.List;

public interface MusicService {

    List<Music> findAll();

    Music save(Music music);

    void delete(Long id);
}
