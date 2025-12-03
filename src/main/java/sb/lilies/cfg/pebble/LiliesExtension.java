package sb.lilies.cfg.pebble;

import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.tokenParser.TokenParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LiliesExtension extends AbstractExtension {

    private final List<TokenParser> parsers = new ArrayList<>();
    private final Map<String, Filter> filterMap = new HashMap<>();

    public LiliesExtension() {
        this.parsers.add(new RegroupParser());
        this.filterMap.put("yes", new YesFilter());
        this.filterMap.put("stars", new StarsFilter(5));
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
