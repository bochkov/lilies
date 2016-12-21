package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.model.Storage;
import com.sergeybochkov.lilies.repository.MusicRepository;
import com.sergeybochkov.lilies.repository.StorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class GenerateFiles implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFiles.class);

    private static final String LY_CMD = "lilypond -dno-point-and-click %s.ly";
    private static final String TIMIDITY_CMD = "timidity --output-24bit -Ow %s.midi";
    private static final String LAME_CMD = "lame -h -b 64 %s.wav %s.mp3";

    private final Music music;
    private final Storage storage;
    private final MusicRepository mRepo;
    private final StorageRepository sRepo;

    public GenerateFiles(MusicRepository mRepo, StorageRepository sRepo, Music music) {
        this.mRepo = mRepo;
        this.sRepo = sRepo;
        this.music = music;
        this.storage = music.getStorage();
    }

    @Override
    public void run() {
        Path tmpFolder = null;
        String baseFilename = music.getBaseFilename();
        try {
            tmpFolder = Files.createTempDirectory(baseFilename);

            File src = new File(tmpFolder.toFile(), baseFilename + ".ly");
            storage.exportSrc(src);
            music.updateSrc(src);
            mRepo.save(music);

            String cmd = String.format(LY_CMD, baseFilename);
            LOG.info(String.format("%s: Начинаем выполнять %s", baseFilename, cmd));
            Runtime.getRuntime().exec(cmd, null, tmpFolder.toFile()).waitFor();
            LOG.info(String.format("%s: Созданы pdf и midi", baseFilename));

            File pdf = new File(tmpFolder.toFile(), baseFilename + ".pdf");
            storage.storePdf(pdf);
            sRepo.save(storage);
            music.updatePdf(pdf);
            mRepo.save(music);

            cmd = String.format(TIMIDITY_CMD, baseFilename);
            LOG.info(String.format("%s: Начинаем выполнять %s", baseFilename, cmd));
            Runtime.getRuntime().exec(cmd, null, tmpFolder.toFile()).waitFor();
            LOG.info(String.format("%s: Создан wav", baseFilename));

            cmd = String.format(LAME_CMD, baseFilename, baseFilename);
            LOG.info(String.format("%s: Начинаем выполнять %s", baseFilename, cmd));
            Runtime.getRuntime().exec(cmd, null, tmpFolder.toFile()).waitFor();
            LOG.info(String.format("%s: Создан mp3", baseFilename));

            File mp3 = new File(tmpFolder.toFile(), baseFilename + ".mp3");
            storage.storeMp3(mp3);
            sRepo.save(storage);
            music.updateMp3(mp3);
            mRepo.save(music);
        }
        catch (IOException | InterruptedException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
        finally {
            LOG.info(String.format("%s: Генерация файлов завершена", baseFilename));
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
