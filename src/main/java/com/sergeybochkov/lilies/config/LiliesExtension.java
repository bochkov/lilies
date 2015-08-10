package com.sergeybochkov.lilies.config;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;

import java.util.HashMap;
import java.util.Map;

public class LiliesExtension extends AbstractExtension {

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> map = new HashMap<>();
        map.put("stars", new StarsFilter());
        return map;
    }
}
