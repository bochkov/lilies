package com.sergeybochkov.lilies.config;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.spring.PebbleViewResolver;
import com.sergeybochkov.lilies.config.pebble.LiliesExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletContext;

@Configuration
public class PebbleResolverConfig {

    @Bean
    public Loader templateLoader() {
        return new ClasspathLoader();
    }

    @Bean
    public PebbleEngine pebbleEngine() {
        PebbleEngine engine = new PebbleEngine(templateLoader());
        engine.addExtension(new LiliesExtension());
        return engine;
    }

    @Bean
    public ViewResolver viewResolver() {
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
