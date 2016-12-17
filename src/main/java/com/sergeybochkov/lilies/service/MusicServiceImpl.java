package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.config.StaticResourceConfig;
import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.repository.MusicRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicServiceImpl implements MusicService {

    public static final int PAGE_SIZE = 50;

    private static final Logger LOG = LoggerFactory.getLogger(MusicServiceImpl.class);

    private final MusicRepository repo;

    @Autowired
    public MusicServiceImpl(MusicRepository repo) {
        this.repo = repo;
    }

    @Override
    public Music findOne(Long id) {
        Music music = repo.findOne(id);
        // if src file not in filesystem - save it
        try {
            File srcFile = new File(StaticResourceConfig.MEDIA_DIR, music.getSrcFilename());
            if (music.getSrcFilename() != null && !srcFile.exists())
                IOUtils.write(music.getSrcFile(), new FileOutputStream(srcFile));
        }
        catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
        // if pdf file not if filesystem - save it
        try {
            File pdfFile = new File(StaticResourceConfig.MEDIA_DIR, music.getPdfFilename());
            if (music.getPdfFilename() != null && !pdfFile.exists())
                IOUtils.write(music.getPdfFile(), new FileOutputStream(pdfFile));
        }
        catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
        // if mp3 file not if filesystem - save it
        try {
            File mp3File = new File(StaticResourceConfig.MEDIA_DIR, music.getMp3Filename());
            if (music.getMp3Filename() != null && !mp3File.exists())
                IOUtils.write(music.getMp3File(), new FileOutputStream(mp3File));
        }
        catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
        return music;
    }

    @Override
    @Transactional
    public List<Music> findAll() {
        return repo.findAll(new Sort(Sort.Direction.ASC, "name", "composer"));
    }

    @Override
    @Transactional
    public Page<Music> findAll(Integer page) {
        return repo.findAll(new PageRequest(page - 1, PAGE_SIZE, Sort.Direction.ASC, "name"));
    }

    @Override
    public Integer pageNum(Music music) {
        int counter = 1;
        int i = 1;
        do {
            Page<Music> page = findAll(counter);
            for (Music m : page)
                if (m.equals(music))
                    return counter;
            if (counter <= page.getTotalPages())
                ++counter;
        }
        while (counter < i);
        return 1;
    }

    @Override
    @Transactional
    public List<Music> findByDifficultyAndInstrumentIn(List<Difficulty> difficulties, List<Instrument> instruments) {
        List<Music> list = new ArrayList<>();
        if (instruments.isEmpty() || difficulties.isEmpty())
            return list;

        repo.findByInstrumentIn(instruments)
                .stream()
                .filter(music -> !list.contains(music))
                .forEach(list::add);

        repo.findByDifficultyIn(difficulties)
                .stream()
                .filter(music -> !list.contains(music))
                .forEach(list::add);

        return list;
    }

    @Override
    @Transactional
    public List<Music> findBySomething(String name) {
        return repo.findBySomething(name);
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
        if (!new File(StaticResourceConfig.MEDIA_DIR, m.getSrcFilename()).delete())
            LOG.warn("Cannot delete " + m.getSrcFilename());
        if (!new File(StaticResourceConfig.MEDIA_DIR, m.getPdfFilename()).delete())
            LOG.warn("Cannot delete " + m.getPdfFilename());
        if (!new File(StaticResourceConfig.MEDIA_DIR, m.getMp3Filename()).delete())
            LOG.warn("Cannot delete " + m.getMp3Filename());
        repo.delete(m);
    }
}
