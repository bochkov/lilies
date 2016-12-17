package com.sergeybochkov.lilies.config.pebble;

import com.mitchellbosecke.pebble.extension.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class StarsFilter implements Filter {

    private static final String EMPTY_STAR = "<i class=\"fa fa-star-o\"></i>";
    private static final String FILL_STAR = "<i class=\"fa fa-star\"></i>";

    private final int maxSize;

    public StarsFilter(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public Object apply(Object input, Map<String, Object> map) {
        StringBuilder html = new StringBuilder();
        Integer value = (Integer) input;
        for (int i = 0; i < value; ++i)
            html.append(FILL_STAR);
        int rang = value;
        while (++rang < maxSize)
            html.append(EMPTY_STAR);
        return html.toString();
    }

    @Override
    public List<String> getArgumentNames() {
        return new ArrayList<>();
    }
}
