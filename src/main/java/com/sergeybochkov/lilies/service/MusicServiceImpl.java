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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicRepository repo;

    @Override
    public Music findOne(Long id) {
        Music music = repo.findOne(id);

        try {
            File srcFile = new File(StaticResourceConfig.MEDIA_DIR + music.getSrcFilename());
            if (music.hasSrc() && music.getSrcFilename() != null && !srcFile.exists())
                IOUtils.write(music.getSrcFile(), new FileWriter(srcFile));
        }
        catch (IOException ex) { ex.printStackTrace(); }

        try {
            File pdfFile = new File(StaticResourceConfig.MEDIA_DIR + music.getPdfFilename());
            if (music.hasPdf() && music.getPdfFilename() != null && !pdfFile.exists())
                IOUtils.write(music.getPdfFile(), new FileWriter(pdfFile));
        }
        catch (IOException ex) { ex.printStackTrace(); }

        try {
            File mp3File = new File(StaticResourceConfig.MEDIA_DIR + music.getMp3Filename());
            if (music.hasMp3() && music.getMp3Filename() != null && !mp3File.exists())
                IOUtils.write(music.getMp3File(), new FileWriter(mp3File));
        }
        catch (IOException ex) { ex.printStackTrace(); }

        return music;
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
    public List<Music> findByNameContainingIgnoreCase(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Music save(Music music) {
        Music m = repo.save(music);
        new GenerateFilesThread(m).start();
        return m;
    }

    @Override
    public void delete(Long id) {
        Music m = findOne(id);
        new File(StaticResourceConfig.MEDIA_DIR + m.getSrcFilename()).delete();
        new File(StaticResourceConfig.MEDIA_DIR + m.getPdfFilename()).delete();
        new File(StaticResourceConfig.MEDIA_DIR + m.getMp3Filename()).delete();
        repo.delete(m);
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
