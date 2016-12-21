package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.repository.MusicRepository;
import com.sergeybochkov.lilies.repository.StorageRepository;
import com.sergeybochkov.lilies.service.generator.GenerateFiles;
import com.sergeybochkov.lilies.web.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
public class MusicServiceImpl implements MusicService {

    public static final int PAGE_SIZE = 25;

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    private final MusicRepository mRepo;
    private final StorageRepository sRepo;

    @Autowired
    public MusicServiceImpl(MusicRepository mRepo, StorageRepository sRepo) {
        this.mRepo = mRepo;
        this.sRepo = sRepo;
    }

    @Override
    public Music findOne(Long id) throws NotFoundException {
        Music music = mRepo.findOne(id);
        if (music == null)
            throw new NotFoundException();
        music.createFiles();
        return music;
    }

    @Override
    public List<Music> findAll() {
        return mRepo.findAll(new Sort(Sort.Direction.ASC, "name", "composer"));
    }

    @Override
    @Transactional
    public Page<Music> findAll(Integer page) {
        return mRepo.findAll(new PageRequest(page - 1, PAGE_SIZE, Sort.Direction.ASC, "name", "composer"));
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
        return mRepo.findByDifficultyInAndInstrumentIn(difficulties, instruments);
    }

    @Override
    public List<Music> findBySomething(String something) {
        return mRepo.findBySomething(String.format("%%%s%%", something));
    }

    @Override
    public Music save(Music music) {
        sRepo.save(music.getStorage());
        return mRepo.save(music);
    }

    @Override
    public Music update(Music music) {
        return mRepo.save(music);
    }

    @Override
    public void generateFiles(Music music) {
        executor.submit(new GenerateFiles(mRepo, sRepo, music));
    }

    @Override
    public void delete(Long id) {
        Music m = findOne(id);
        m.deleteFiles();
        mRepo.delete(m);
    }
}
