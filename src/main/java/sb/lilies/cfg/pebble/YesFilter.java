package sb.lilies.cfg.pebble;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

public final class YesFilter implements Filter {

    @Override
    public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                        EvaluationContext context, int lineNumber) throws PebbleException {
        return Boolean.TRUE.equals(input) ?
                "<i class='fa icon-success fa-check-circle'></i>" :
                "<i class='fa icon-error fa-minus-circle'></i>";
    }

    @Override
    public List<String> getArgumentNames() {
        return new ArrayList<>();
    }
}
