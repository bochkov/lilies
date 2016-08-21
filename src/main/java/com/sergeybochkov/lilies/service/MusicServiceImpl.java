package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.config.StaticResourceConfig;
import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.repository.MusicRepository;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicServiceImpl implements MusicService {

    public static final int PAGE_SIZE = 50;

    private static final Logger LOG = Logger.getLogger(MusicServiceImpl.class);

    private final MusicRepository repo;

    @Autowired
    public MusicServiceImpl(MusicRepository repo) {
        this.repo = repo;
    }

    @Override
    public Music findOne(Long id) {
        Music music = repo.findOne(id);

        try {
            File srcFile = new File(StaticResourceConfig.MEDIA_DIR, music.getSrcFilename());
            if (music.getSrcFilename() != null && !srcFile.exists())
                IOUtils.write(music.getSrcFile(), new FileOutputStream(srcFile));
            if (!music.hasSrc() && music.getSrcFile() != null && music.getSrcFile().length > 0) {
                music.setSrcFileLength((long) music.getSrcFile().length);
                repo.save(music);
            }
        }
        catch (IOException ex) { LOG.warn(ex); }

        try {
            File pdfFile = new File(StaticResourceConfig.MEDIA_DIR, music.getPdfFilename());
            if (music.getPdfFilename() != null && !pdfFile.exists())
                IOUtils.write(music.getPdfFile(), new FileOutputStream(pdfFile));
            if (!music.hasPdf() && music.getPdfFile() != null && music.getPdfFile().length > 0) {
                music.setPdfFileLength((long) music.getPdfFile().length);
                repo.save(music);
            }
        }
        catch (IOException ex) { LOG.warn(ex); }

        try {
            File mp3File = new File(StaticResourceConfig.MEDIA_DIR, music.getMp3Filename());
            if (music.getMp3Filename() != null && !mp3File.exists())
                IOUtils.write(music.getMp3File(), new FileOutputStream(mp3File));
            if (!music.hasMp3() && music.getMp3File() != null && music.getMp3File().length > 0) {
                music.setMp3FileLength((long) music.getMp3File().length);
                repo.save(music);
            }
        }
        catch (IOException ex) { LOG.warn(ex); }

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
        if (!new File(StaticResourceConfig.MEDIA_DIR, m.getSrcFilename()).delete())
            LOG.warn("Cannot delete " + m.getSrcFilename());
        if (!new File(StaticResourceConfig.MEDIA_DIR, m.getPdfFilename()).delete())
            LOG.warn("Cannot delete " + m.getPdfFilename());
        if (!new File(StaticResourceConfig.MEDIA_DIR, m.getMp3Filename()).delete())
            LOG.warn("Cannot delete " + m.getMp3Filename());

        repo.delete(m);
    }

    private class GenerateFilesThread extends Thread {

        private static final String LY_CMD = "lilypond -dno-point-and-click %s";
        private static final String TIMIDITY_CMD = "timidity --output-24bit -Ow  %s.midi";
        private static final String LAME_CMD = "lame -h -b 64 %s.wav %s.mp3";

        private Music music;

        public GenerateFilesThread(Music music) {
            this.music = music;
        }

        @Override
        public void run() {
            try {
                Path path = Files.createTempDirectory(music.getBaseFilename());

                String lyFn = music.getBaseFilename() + ".ly";
                File lyFile = new File(path.toFile(), lyFn);

                String pdfFn = music.getBaseFilename() + ".pdf";
                File pdfFile = new File(path.toFile(), pdfFn);

                String mp3Fn = music.getBaseFilename() + ".mp3";
                File mp3File = new File(path.toFile(), mp3Fn);

                IOUtils.write(music.getSrcFile(), new FileOutputStream(lyFile));
                music.setSrcFilename("src/" + lyFn);
                music.setSrcFileLength(lyFile.length());

                String cmd = String.format(LY_CMD, lyFn);
                LOG.info(lyFn + ": Начинаем выполнять " + cmd);
                Runtime.getRuntime()
                        .exec(cmd, null, path.toFile())
                        .waitFor();
                LOG.info(lyFn + ": Создание pdf и midi для выполнено");

                music.setPdfFilename("pdf/" + pdfFn);
                music.setPdfFileLength(pdfFile.length());
                music.setPdfFile(IOUtils.toByteArray(new FileInputStream(pdfFile)));

                cmd = String.format(TIMIDITY_CMD, music.getBaseFilename());
                LOG.info(lyFn + ": Начинаем выполнять " + cmd);
                Runtime.getRuntime()
                        .exec(cmd, null, path.toFile())
                        .waitFor();
                LOG.info(lyFn + ": Выполнено создание wav");

                cmd = String.format(LAME_CMD, music.getBaseFilename(), music.getBaseFilename());
                LOG.info(lyFn + ": Начинаем выполнять " + cmd);
                Runtime.getRuntime()
                        .exec(cmd, null, path.toFile())
                        .waitFor();
                LOG.info(lyFn + ": Выполнено создание mp3");

                music.setMp3Filename("mp3/" + mp3Fn);
                music.setMp3FileLength(mp3File.length());
                music.setMp3File(IOUtils.toByteArray(new FileInputStream(mp3File)));

                LOG.info(lyFn + ": Завершена генерация файлов для произведения " + music.getName());
                delete(path);
            }
            catch (IOException | InterruptedException ex) {
                LOG.warn(ex.getMessage());
            }
            finally {
                repo.save(music);
            }
        }

        private void delete(Path path) throws IOException {
            File folder = path.toFile();
            File[] files = folder.listFiles();
            if (files != null)
                for (File file : files)
                    if (!file.delete())
                        LOG.info("Ошибка удаления файла: " + file.getAbsolutePath());

            Files.delete(path);
        }
    }
}
