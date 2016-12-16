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

    private final DifficultyService difficultyService;

    public LiliesExtension(DifficultyService difficultyService) {
        this.difficultyService = difficultyService;
    }

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> map = new HashMap<>();
        map.put("stars", new StarsFilter(difficultyService));
        map.put("yes", new YesFilter());
        return map;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        List<TokenParser> list = new ArrayList<>();
        list.add(new RegroupParser());
        return list;
    }
}
