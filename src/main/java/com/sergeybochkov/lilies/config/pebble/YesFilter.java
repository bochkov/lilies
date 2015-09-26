package com.sergeybochkov.lilies.config.pebble;

import com.mitchellbosecke.pebble.extension.Filter;

import java.util.List;
import java.util.Map;


public class YesFilter implements Filter {

    @Override
    public Object apply(Object input, Map<String, Object> map) {
        if (input == null)
            return null;

        Boolean in = (Boolean) input;
        String html;
        if (in)
            html = "<i class='fa icon-success fa-check-circle'></i>";
        else
            html = "<i class='fa icon-error fa-minus-circle'></i>";

        return html;
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
