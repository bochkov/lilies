package com.sergeybochkov.lilies.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

@Configuration
public class StaticResourceConfig extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(StaticResourceConfig.class);

    public static final String MEDIA_DIR = System.getProperty("user.dir") + "/media/";
    public static final String MEDIA_URL = "/media/**";

    private void createDirs(String parent, String... dirs) {
        File file = new File(parent);
        if (!file.exists() && !file.mkdirs())
            LOG.warn(String.format("Media dir '%s' not created", parent));
        else {
            for (String dir : dirs) {
                File f = new File(file, dir);
                if (!f.exists() && !f.mkdirs())
                    LOG.warn(String.format("Dir '%s' not created", dir));
            }
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        createDirs(MEDIA_DIR, "src", "pdf", "mp3");
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler("favicon.ico")
                .addResourceLocations("classpath:/favicon.ico");
        registry
                .addResourceHandler(MEDIA_URL)
                .addResourceLocations("file:" + MEDIA_DIR);
    }
}
