package sb.lilies.page;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.RequiredArgsConstructor;
import ratpack.core.error.ServerErrorHandler;
import ratpack.core.handling.Context;

@RequiredArgsConstructor
public final class ServerErrorPage implements ServerErrorHandler {

    private final PebbleEngine pebble;

    @Override
    public void error(Context context, Throwable throwable) throws Exception {
        Map<String, Object> map = new HashMap<>();
        StringWriter out = new StringWriter();
        pebble.getTemplate("templates/error/500.html")
                .evaluate(out, map);
        context.header("Content-Type", "text/html")
                .render(out.toString());
    }
}
