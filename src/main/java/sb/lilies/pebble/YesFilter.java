package sb.lilies.pebble;

import com.mitchellbosecke.pebble.extension.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class YesFilter implements Filter {

    @Override
    public Object apply(Object input, Map<String, Object> map) {
        return ((Boolean) input) ?
                "<i class='fa icon-success fa-check-circle'></i>" :
                "<i class='fa icon-error fa-minus-circle'></i>";
    }

    @Override
    public List<String> getArgumentNames() {
        return new ArrayList<>();
    }
}
