package com.sergeybochkov.lilies.config;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.spring.PebbleViewResolver;
import com.sergeybochkov.lilies.config.pebble.LiliesExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

@Configuration
public class PebbleResolverConfig {
    @Bean
    public ViewResolver viewResolver() {
        PebbleViewResolver resolver = new PebbleViewResolver();
        resolver.setPrefix("templates");
        resolver.setSuffix(".html");
        PebbleEngine engine = new PebbleEngine();

        // if angularjs used
        /*
        Lexer lexer = engine.getLexer();
        lexer.setPrintOpenDelimiter("<<");
        lexer.setPrintCloseDelimiter(">>");
        */

        engine.addExtension(new LiliesExtension());
        resolver.setPebbleEngine(engine);
        return resolver;
    }
}
