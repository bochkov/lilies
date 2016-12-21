package com.sergeybochkov.lilies.service.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public final class Lilypond implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Lilypond.class);

    private static final String LY_CMD = "lilypond -dno-point-and-click %s.ly";

    private final String baseFilename;
    private final File directory;

    public Lilypond(String baseFilename, File directory) {
        this.baseFilename = baseFilename;
        this.directory = directory;
    }

    @Override
    public File produce() throws IOException, InterruptedException {
        String cmd = String.format(LY_CMD, baseFilename);
        LOG.info(String.format("%s: Начинаем выполнять %s", baseFilename, cmd));
        Runtime.getRuntime().exec(cmd, null, directory).waitFor();
        LOG.info(String.format("%s: Созданы pdf и midi", baseFilename));
        return new File(directory, baseFilename + ".pdf");
    }
}
