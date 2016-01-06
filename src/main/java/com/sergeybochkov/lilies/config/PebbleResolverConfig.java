package com.sergeybochkov.lilies.config;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.spring.PebbleViewResolver;
import com.sergeybochkov.lilies.config.pebble.LiliesExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class PebbleResolverConfig extends WebMvcConfigurerAdapter {

    @Bean
    public Loader templateLoader() {
        return new ClasspathLoader();
    }

    @Bean
    public PebbleEngine pebbleEngine() {
        PebbleEngine.Builder engineBuilder = new PebbleEngine.Builder();
        engineBuilder.extension(new LiliesExtension());
        return engineBuilder.build();
    }

    @Bean
    public ViewResolver pebbleViewResolver() {
        PebbleViewResolver resolver = new PebbleViewResolver();
        resolver.setPrefix("templates");
        resolver.setSuffix(".html");

        // if angularjs used
        /*
        Lexer lexer = engine.getLexer();
        lexer.setPrintOpenDelimiter("<<");
        lexer.setPrintCloseDelimiter(">>");
        */

        resolver.setPebbleEngine(pebbleEngine());
        return resolver;
    }
}
