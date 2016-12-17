package com.sergeybochkov.lilies.config.pebble;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;
import com.sergeybochkov.lilies.service.DifficultyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LiliesExtension extends AbstractExtension {

    private final List<TokenParser> parsers;
    private final Map<String, Filter> filterMap;

    public LiliesExtension(DifficultyService difficultyService) {
        this.parsers = new ArrayList<>();
        this.parsers.add(new RegroupParser());
        this.filterMap = new HashMap<>();
        this.filterMap.put("yes", new YesFilter());
        this.filterMap.put("stars", new StarsFilter(difficultyService.findAll().size()));
    }

    @Override
    public Map<String, Filter> getFilters() {
        return filterMap;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        return parsers;
    }
}
