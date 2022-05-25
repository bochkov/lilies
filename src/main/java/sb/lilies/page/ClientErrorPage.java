package sb.lilies.page;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.RequiredArgsConstructor;
import ratpack.core.error.ClientErrorHandler;
import ratpack.core.handling.Context;

@RequiredArgsConstructor
public final class ClientErrorPage implements ClientErrorHandler {

    private final PebbleEngine pebble;

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
