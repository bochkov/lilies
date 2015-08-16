package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.config.StaticResourceConfig;
import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.repository.MusicRepository;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicRepository repo;

    @Override
    public Music findOne(Long id) {
        return repo.findOne(id);
    }

    @Override
    public List<Music> findAll() {
        return repo.findAll();
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
    public Music save(Music music) {
        Music m = repo.save(music);
        new GenerateFilesThread(m).start();
        return m;
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    private class GenerateFilesThread extends Thread {

        private final Logger log = Logger.getLogger(GenerateFilesThread.class.getName());
        private Music music;

        public GenerateFilesThread(Music music) {
            this.music = music;
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
                music.setPdfFile(IOUtils.toByteArray(new FileInputStream(new File(media, music.getPdfFilename()))));

                Runtime.getRuntime().exec("timidity " + name + ".midi -Ow", null, media).waitFor();
                Runtime.getRuntime().exec("lame -h -b 128 " + name + ".wav" + " " + name + ".mp3", null, media).waitFor();
                new File(media, name + ".midi").delete();
                new File(media, name + ".wav").delete();
                music.setMp3Filename("mp3/" + name + ".mp3");
                new File(media, name + ".mp3").renameTo(new File(media, music.getMp3Filename()));
                music.setMp3File(IOUtils.toByteArray(new FileInputStream(new File(media, music.getMp3Filename()))));

                log.info("Генерация файлов завершена");
            }
            catch (IOException | InterruptedException ex) {
                log.warn(ex.getMessage());
            }
            finally {
                repo.save(music);
            }
        }
    }
}
