package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.repository.MusicRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public final class GenerateFiles extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFiles.class);

    private static final String LY_CMD = "lilypond -dno-point-and-click %s.ly";
    private static final String TIMIDITY_CMD = "timidity --output-24bit -Ow %s.midi";
    private static final String LAME_CMD = "lame -h -b 64 %s.wav %s.mp3";

    private final Music music;
    private final MusicRepository repo;

    public GenerateFiles(MusicRepository repo, Music music) {
        this.repo = repo;
        this.music = music;
    }

    @Override
    public void run() {
        Path tmpFolder = null;
        try {
            tmpFolder = Files.createTempDirectory(music.getBaseFilename());
            //
            File lyFile = new File(tmpFolder.toFile(), music.getBaseFilename() + ".ly");
            IOUtils.write(music.getSrcFile(), new FileOutputStream(lyFile));
            music.updateSrc(lyFile);
            repo.save(music);

            String cmd = String.format(LY_CMD, music.getBaseFilename());
            LOG.info(String.format("%s: Начинаем выполнять %s", music.getBaseFilename(), cmd));
            Runtime.getRuntime().exec(cmd, null, tmpFolder.toFile()).waitFor();
            LOG.info(String.format("%s: Созданы pdf и midi", music.getBaseFilename()));

            music.updatePdf(new File(tmpFolder.toFile(), music.getBaseFilename() + ".pdf"));
            repo.save(music);

            cmd = String.format(TIMIDITY_CMD, music.getBaseFilename());
            LOG.info(String.format("%s: Начинаем выполнять %s", music.getBaseFilename(), cmd));
            Runtime.getRuntime().exec(cmd, null, tmpFolder.toFile()).waitFor();
            LOG.info(String.format("%s: Создан wav", music.getBaseFilename()));

            cmd = String.format(LAME_CMD, music.getBaseFilename(), music.getBaseFilename());
            LOG.info(String.format("%s: Начинаем выполнять %s", music.getBaseFilename(), cmd));
            Runtime.getRuntime().exec(cmd, null, tmpFolder.toFile()).waitFor();
            LOG.info(String.format("%s: Создан mp3", music.getBaseFilename()));

            music.updateMp3(new File(tmpFolder.toFile(), music.getBaseFilename() + ".mp3"));
            repo.save(music);
        }
        catch (IOException | InterruptedException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
        finally {
            LOG.info(String.format("%s: Генерация файлов завершена", music.getBaseFilename()));
            if (tmpFolder != null)
                delete(tmpFolder);
        }
    }

    private void delete(Path path) {
        File[] files = path.toFile().listFiles();
        if (files != null)
            for (File file : files)
                if (!file.delete())
                    LOG.info("Ошибка удаления файла: " + file.getAbsolutePath());
        try {
            Files.delete(path);
        }
        catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }
}
