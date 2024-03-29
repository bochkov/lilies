package sb.lilies.cfg.pebble;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class StarsFilter implements Filter {

    private static final String EMPTY_STAR = "<i class=\"fas fa-star-o\"></i>";
    private static final String FILL_STAR = "<i class=\"fas fa-star\"></i>";

    private final int maxSize;

    @Override
    public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                        EvaluationContext context, int lineNumber) throws PebbleException {
        StringBuilder html = new StringBuilder();
        Integer value = (Integer) input;
        html.append(FILL_STAR.repeat(Math.max(0, value)));
        int rang = value;
        while (rang++ < maxSize)
            html.append(EMPTY_STAR);
        return html.toString();
    }

    @Override
    public List<String> getArgumentNames() {
        return new ArrayList<>();
    }
}
