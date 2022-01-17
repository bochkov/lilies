package sb.lilies.page;

import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.RequiredArgsConstructor;
import ratpack.handling.Context;
import sb.lilies.PgInstruments;
import sb.lilies.PgMusics;

import javax.sql.DataSource;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class AboutPage implements Page {

    private final DataSource ds;
    private final PebbleEngine pebble;

    @Override
    public void handle(Context ctx) throws Exception {
        StringWriter str = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("sheets", new PgMusics(ds).count());
        map.put("instruments", new PgInstruments(ds).count());
        pebble.getTemplate("templates/lilies/about.html")
                .evaluate(str, map);
        ctx.header("Content-Type", "text/html")
                .render(str.toString());
    }
}
