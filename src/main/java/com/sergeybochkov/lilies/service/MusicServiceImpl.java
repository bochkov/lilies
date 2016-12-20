package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.repository.MusicRepository;
import com.sergeybochkov.lilies.web.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public final class MusicServiceImpl implements MusicService {

    public static final int PAGE_SIZE = 25;

    private final MusicRepository repo;

    @Autowired
    public MusicServiceImpl(MusicRepository repo) {
        this.repo = repo;
    }

    @Override
    public Music findOne(Long id) throws NotFoundException {
        Music music = repo.findOne(id);
        if (music == null)
            throw new NotFoundException();
        music.createFiles();
        return music;
    }

    @Override
    public List<Music> findAll() {
        return repo.findAll(new Sort(Sort.Direction.ASC, "name", "composer"));
    }

    @Override
    public Page<Music> findAll(Integer page) {
        return repo.findAll(new PageRequest(page - 1, PAGE_SIZE, Sort.Direction.ASC, "id"));
    }

    @Override
    public Integer pageNum(Music music) {
        int curPage = 1;
        int totalPages;
        do {
            Page<Music> page = findAll(curPage);
            totalPages = page.getTotalPages();
            if (page.getContent().contains(music)) {
                return curPage;
            }
            ++curPage;
        } while (curPage <= totalPages);
        return 1;
    }

    @Override
    public List<Music> findByDifficultyAndInstrumentIn(List<Difficulty> difficulties, List<Instrument> instruments) {
        if (difficulties.isEmpty() || instruments.isEmpty())
            return new ArrayList<>();
        return repo.findByDifficultyInAndInstrumentIn(difficulties, instruments);
    }

    @Override
    public List<Music> findBySomething(String something) {
        return repo.findBySomething(String.format("%%%s%%", something));
    }

    @Override
    public Music save(Music music) {
        return repo.save(music);
    }

    @Override
    public void generateFiles(Music music) {
        new GenerateFiles(repo, music).start();
    }

    @Override
    public void delete(Long id) {
        Music m = findOne(id);
        m.deleteFiles();
        repo.delete(m);
    }
}
