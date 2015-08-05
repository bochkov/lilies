package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicRepository repo;

    @Override
    public List<Music> findAll() {
        return repo.findAll();
    }

    @Override
    public Music save(Music music) {
        return repo.save(music);
    }
}
