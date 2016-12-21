package com.sergeybochkov.lilies.service.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public final class Lame implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Lame.class);

    private static final String LAME_CMD = "lame -S -f -b 64 %s.wav %s.mp3";

    private final String baseFilename;
    private final File directory;

    public Lame(String baseFilename, File directory) {
        this.baseFilename = baseFilename;
        this.directory = directory;
    }

    @Override
    public File produce() throws IOException, InterruptedException {
        String cmd = String.format(LAME_CMD, baseFilename, baseFilename);
        LOG.info(String.format("%s: Начинаем выполнять %s", baseFilename, cmd));
        Runtime.getRuntime().exec(cmd, null, directory).waitFor();
        LOG.info(String.format("%s: Создан mp3", baseFilename));
        return new File(directory, baseFilename + ".mp3");
    }
}
