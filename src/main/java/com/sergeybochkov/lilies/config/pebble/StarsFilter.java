package com.sergeybochkov.lilies.config.pebble;

import com.coverity.security.Escape;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.escaper.RawFilter;

import java.util.List;
import java.util.Map;

public class StarsFilter implements Filter {

    @Override
    public Object apply(Object input, Map<String, Object> map) {
        if (input == null)
            return null;

        int len = 5;
        String html = "";
        Integer value = (Integer) input;
        for (int i = 0; i < value; ++i)
            html += "<i class=\"glyphicon glyphicon-star\"></i>";
        int rang = value;
        while (rang < len) {
            html += "<i class=\"glyphicon glyphicon-star-empty\"></i>";
            ++rang;
        }
        return html;
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
