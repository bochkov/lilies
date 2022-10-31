package sb.lilies.cfg.pebble;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

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
