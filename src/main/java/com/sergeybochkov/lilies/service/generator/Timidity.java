package com.sergeybochkov.lilies.service.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public final class Timidity implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Timidity.class);

    private static final String TIMIDITY_CMD = "timidity --output-24bit -Ow %s.midi";

    private final String baseFilename;
    private final File directory;

    public Timidity(String baseFilename, File directory) {
        this.baseFilename = baseFilename;
        this.directory = directory;
    }

    @Override
    public File produce() throws IOException, InterruptedException {
        String cmd = String.format(TIMIDITY_CMD, baseFilename);
        LOG.info(String.format("%s: Начинаем выполнять %s", baseFilename, cmd));
        Runtime.getRuntime().exec(cmd, null, directory).waitFor();
        LOG.info(String.format("%s: Создан wav", baseFilename));
        return new File(directory, baseFilename + ".wav");
    }
}
