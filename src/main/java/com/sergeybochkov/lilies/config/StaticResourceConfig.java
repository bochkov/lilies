package com.sergeybochkov.lilies.config;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.File;

@Configuration
public class StaticResourceConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    public static final String MEDIA_DIR = System.getProperty("user.dir") + "/media/";
    public static final String MEDIA_URL = "/media/**";

    private void createDir(String path) {
        File file = new File(path);
        if (!file.exists() && !file.mkdirs())
            System.out.println(path + " not created");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        createDir(MEDIA_DIR);
        createDir(MEDIA_DIR + "src");
        createDir(MEDIA_DIR + "pdf");
        createDir(MEDIA_DIR + "mp3");

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
