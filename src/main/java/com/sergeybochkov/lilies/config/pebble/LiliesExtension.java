package com.sergeybochkov.lilies.config.pebble;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiliesExtension extends AbstractExtension {

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> map = new HashMap<>();
        map.put("stars", new StarsFilter());
        return map;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        List<TokenParser> list = new ArrayList<>();
        list.add(new RegroupToken());
        return list;
    }
}
