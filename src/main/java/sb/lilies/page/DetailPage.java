package sb.lilies.page;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.RequiredArgsConstructor;
import ratpack.core.handling.Context;
import sb.lilies.CtMusics;

@RequiredArgsConstructor
public final class DetailPage implements Page {

    private final DataSource ds;
    private final PebbleEngine pebble;

    @Override
    public void handle(Context ctx) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("object", new CtMusics(ds).find(ctx.getAllPathTokens().asInt("id")));
        try {
            StringWriter str = new StringWriter();
            pebble.getTemplate("templates/lilies/detail.html")
                    .evaluate(str, map);
            ctx.header("Content-Type", "text/html")
                    .render(str.toString());
        } catch (Exception ex) {
            ctx.clientError(404);
        }
    }
}
