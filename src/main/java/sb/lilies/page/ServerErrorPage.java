package sb.lilies.page;

import com.mitchellbosecke.pebble.PebbleEngine;
import ratpack.error.ServerErrorHandler;
import ratpack.handling.Context;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class ServerErrorPage implements ServerErrorHandler {

    private final PebbleEngine pebble;

    public ServerErrorPage(PebbleEngine pebble) {
        this.pebble = pebble;
    }

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
