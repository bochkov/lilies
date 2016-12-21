package com.sergeybochkov.lilies.service.generator;

import java.io.File;
import java.io.IOException;

public interface Command {

    File produce() throws IOException, InterruptedException;
}
