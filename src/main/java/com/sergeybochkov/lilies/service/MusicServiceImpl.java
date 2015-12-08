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

    @Autowired
    private MusicRepository repo;
    @Autowired
    private StorageRepository stRepo;

    @Override
    public Music findOne(Long id) {
        Music music = repo.findOne(id);
        Storage storage = stRepo.findOne(music.getId());

        try {
            File srcFile = new File(StaticResourceConfig.MEDIA_DIR + music.getSrcFilename());
            if (storage.hasSrc() && music.getSrcFilename() != null && !srcFile.exists())
                IOUtils.write(storage.getSrcFile(), new FileOutputStream(srcFile));
        }
        catch (IOException ex) { ex.printStackTrace(); }

        try {
            File pdfFile = new File(StaticResourceConfig.MEDIA_DIR + music.getPdfFilename());
            if (storage.hasPdf() && music.getPdfFilename() != null && !pdfFile.exists())
                IOUtils.write(storage.getPdfFile(), new FileOutputStream(pdfFile));
        }
        catch (IOException ex) { ex.printStackTrace(); }

        try {
            File mp3File = new File(StaticResourceConfig.MEDIA_DIR + music.getMp3Filename());
            if (storage.hasMp3() && music.getMp3Filename() != null && !mp3File.exists())
                IOUtils.write(storage.getMp3File(), new FileOutputStream(mp3File));
        }
        catch (IOException ex) { ex.printStackTrace(); }

        return music;
    }

    @Override
    public List<Music> findAll() {
        return repo.findAll(new Sort(Sort.Direction.ASC, "name", "composer"));
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
        new File(StaticResourceConfig.MEDIA_DIR + m.getSrcFilename()).delete();
        new File(StaticResourceConfig.MEDIA_DIR + m.getPdfFilename()).delete();
        new File(StaticResourceConfig.MEDIA_DIR + m.getMp3Filename()).delete();
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

        private final Logger log = Logger.getLogger(GenerateFilesThread.class.getName());
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

                Runtime.getRuntime().exec("lilypond -dno-point-and-click " + name, null, media).waitFor();
                music.setSrcFilename("src/" + name + ".ly");
                new File(media, name + ".ly").renameTo(new File(media, music.getSrcFilename()));

                music.setPdfFilename("pdf/" + name + ".pdf");
                new File(media, name + ".pdf").renameTo(new File(media, music.getPdfFilename()));
                storage.setPdfFile(IOUtils.toByteArray(new FileInputStream(new File(media, music.getPdfFilename()))));

                Runtime.getRuntime().exec("timidity " + name + ".midi -Ow", null, media).waitFor();
                Runtime.getRuntime().exec("lame -h -b 128 " + name + ".wav" + " " + name + ".mp3", null, media).waitFor();
                new File(media, name + ".midi").delete();
                new File(media, name + ".wav").delete();
                music.setMp3Filename("mp3/" + name + ".mp3");
                new File(media, name + ".mp3").renameTo(new File(media, music.getMp3Filename()));
                storage.setMp3File(IOUtils.toByteArray(new FileInputStream(new File(media, music.getMp3Filename()))));

                log.info("Завершена генерация файлов для произведения " + music.getName());
            }
            catch (IOException | InterruptedException ex) {
                log.warn(ex.getMessage());
            }
            finally {
                repo.save(music);
                stRepo.save(storage);
            }
        }
    }
}
