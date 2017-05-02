package sb.lilies.page;

import com.mitchellbosecke.pebble.PebbleEngine;
import ratpack.error.ClientErrorHandler;
import ratpack.handling.Context;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class ClientErrorPage implements ClientErrorHandler {

    private final PebbleEngine pebble;

    public ClientErrorPage(PebbleEngine pebble) {
        this.pebble = pebble;
    }

    @Override
    public void error(Context context, int statusCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        StringWriter out = new StringWriter();
        pebble.getTemplate("templates/error/404.html")
                .evaluate(out, map);
        context.header("Content-Type", "text/html")
                .render(out.toString());
    }
}
