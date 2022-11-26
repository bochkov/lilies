package sb.lilies.cfg.pebble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.tokenParser.TokenParser;

public final class LiliesExtension extends AbstractExtension {

    private final List<TokenParser> parsers = new ArrayList<>();
    private final Map<String, Filter> filterMap = new HashMap<>();

    public LiliesExtension(int maxDiff) {
        this.parsers.add(new RegroupParser());
        this.filterMap.put("yes", new YesFilter());
        this.filterMap.put("stars", new StarsFilter(maxDiff));
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
