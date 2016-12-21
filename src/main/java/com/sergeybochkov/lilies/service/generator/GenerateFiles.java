package com.sergeybochkov.lilies.service.generator;

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
            music.updateSrc(new File(tmpFolder.toFile(), baseFilename + ".ly"));
            mRepo.save(music);

            File pdf = new Lilypond(baseFilename, tmpFolder.toFile()).produce();
            storage.storePdf(pdf);
            sRepo.save(storage);
            music.updatePdf(pdf);
            mRepo.save(music);

            new Timidity(baseFilename, tmpFolder.toFile()).produce(); // produces wav
            File mp3 = new Lame(baseFilename, tmpFolder.toFile()).produce();
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
