package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.config.StaticResourceConfig;
import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.model.Storage;
import com.sergeybochkov.lilies.repository.MusicRepository;
import com.sergeybochkov.lilies.repository.StorageRepository;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicServiceImpl implements MusicService {

    public static final int PAGE_SIZE = 50;

    private static final Logger LOG = Logger.getLogger(MusicServiceImpl.class);

    private final MusicRepository repo;
    private final StorageRepository stRepo;

    @Autowired
    public MusicServiceImpl(MusicRepository repo, StorageRepository stRepo) {
        this.repo = repo;
        this.stRepo = stRepo;
    }

    @Override
    public Music findOne(Long id) {
        Music music = repo.findOne(id);
        Storage storage = stRepo.findOne(music.getId());

        try {
            File srcFile = new File(StaticResourceConfig.MEDIA_DIR + music.getSrcFilename());
            if (music.getSrcFilename() != null && !srcFile.exists())
                IOUtils.write(storage.getSrcFile(), new FileOutputStream(srcFile));
            if (!music.hasSrc() && storage.getSrcFile() != null && storage.getSrcFile().length > 0) {
                music.setSrcFileLength((long) storage.getSrcFile().length);
                repo.save(music);
            }
        }
        catch (IOException ex) { ex.printStackTrace(); }

        try {
            File pdfFile = new File(StaticResourceConfig.MEDIA_DIR + music.getPdfFilename());
            if (music.getPdfFilename() != null && !pdfFile.exists())
                IOUtils.write(storage.getPdfFile(), new FileOutputStream(pdfFile));
            if (!music.hasPdf() && storage.getPdfFile() != null && storage.getPdfFile().length > 0) {
                music.setPdfFileLength((long) storage.getPdfFile().length);
                repo.save(music);
            }
        }
        catch (IOException ex) { ex.printStackTrace(); }

        try {
            File mp3File = new File(StaticResourceConfig.MEDIA_DIR + music.getMp3Filename());
            if (music.getMp3Filename() != null && !mp3File.exists())
                IOUtils.write(storage.getMp3File(), new FileOutputStream(mp3File));
            if (!music.hasMp3() && storage.getMp3File() != null && storage.getMp3File().length > 0) {
                music.setMp3FileLength((long) storage.getMp3File().length);
                repo.save(music);
            }
        }
        catch (IOException ex) { ex.printStackTrace(); }

        return music;
    }

    @Override
    public List<Music> findAll() {
        return repo.findAll(new Sort(Sort.Direction.ASC, "name", "composer"));
    }

    @Override
    public Page<Music> findAll(Integer page) {
        PageRequest pr = new PageRequest(page - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
        return repo.findAll(pr);
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
        return null;
    }

    @Override
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
    public List<Music> findBySomething(String name) {
        return repo.findBySomething("%" + name + "%");
    }

    @Override
    public Music save(Music music) {
        return repo.save(music);
    }

    @Override
    public void generateFiles(Music music) {
        new GenerateFilesThread(music).start();
    }

    @Override
    public void delete(Long id) {
        Music m = findOne(id);
        if (!new File(StaticResourceConfig.MEDIA_DIR + m.getSrcFilename()).delete())
            LOG.warn("Cannot delete " + m.getSrcFilename());
        if (!new File(StaticResourceConfig.MEDIA_DIR + m.getPdfFilename()).delete())
            LOG.warn("Cannot delete " + m.getPdfFilename());
        if (!new File(StaticResourceConfig.MEDIA_DIR + m.getMp3Filename()).delete())
            LOG.warn("Cannot delete " + m.getMp3Filename());
        stRepo.delete(id);
        repo.delete(m);
    }

    @Override
    public Storage getStorage(Music music) {
        Storage st = music.getStorage();
        return st == null ? new Storage(music) : st;
    }

    @Override
    public Storage save(Storage storage) {
        return stRepo.save(storage);
    }

    private class GenerateFilesThread extends Thread {

        private Music music;
        private Storage storage;

        public GenerateFilesThread(Music music) {
            this.music = music;
            this.storage = getStorage(music);
        }

        @Override
        public void run() {
            try {
                String name = music.getSrcFilename().substring(0, music.getSrcFilename().lastIndexOf("."));
                File media = new File(StaticResourceConfig.MEDIA_DIR);

                File srcFile = new File(media, music.getSrcFilename());
                Runtime.getRuntime().exec("lilypond -dno-point-and-click " + name, null, media).waitFor();
                music.setSrcFilename("src/" + name + ".ly");
                music.setSrcFileLength(srcFile.length());
                if (!new File(media, name + ".ly").renameTo(srcFile))
                    LOG.warn("Cannot rename to " + srcFile.getName());

                music.setPdfFilename("pdf/" + name + ".pdf");
                File pdfFile = new File(media, music.getPdfFilename());
                music.setPdfFileLength(pdfFile.length());
                if (!new File(media, name + ".pdf").renameTo(pdfFile))
                    LOG.warn("Cannot rename to " + pdfFile.getName());
                storage.setPdfFile(IOUtils.toByteArray(new FileInputStream(pdfFile)));

                Runtime.getRuntime().exec("timidity " + name + ".midi -Ow", null, media).waitFor();
                Runtime.getRuntime().exec("lame -h -b 128 " + name + ".wav" + " " + name + ".mp3", null, media).waitFor();
                if (!new File(media, name + ".midi").delete())
                    LOG.warn("Cannot delete " + name + ".midi");
                if (!new File(media, name + ".wav").delete())
                    LOG.warn("Cannot delete " + name + ".wav");
                music.setMp3Filename("mp3/" + name + ".mp3");
                File mp3File = new File(media, music.getMp3Filename());
                music.setMp3FileLength(mp3File.length());
                if (!new File(media, name + ".mp3").renameTo(mp3File))
                    LOG.warn("Cannot delete " + name + ".mp3");
                storage.setMp3File(IOUtils.toByteArray(new FileInputStream(mp3File)));

                LOG.info("Завершена генерация файлов для произведения " + music.getName());
            }
            catch (IOException | InterruptedException ex) {
                LOG.warn(ex.getMessage());
            }
            finally {
                repo.save(music);
                stRepo.save(storage);
            }
        }
    }
}
