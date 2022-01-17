package sb.lilies.page;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.RequiredArgsConstructor;
import ratpack.handling.Context;
import sb.lilies.CtDifficulties;
import sb.lilies.CtInstruments;

@RequiredArgsConstructor
public final class IndexPage implements Page {

    private final DataSource ds;
    private final PebbleEngine pebble;

    @Override
    public void handle(Context ctx) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("instruments", new CtInstruments(ds).iterate());
        map.put("difficulties", new CtDifficulties(ds).iterate());
        StringWriter str = new StringWriter();
        pebble.getTemplate("templates/lilies/index.html")
                .evaluate(str, map);
        ctx.header("Content-Type", "text/html")
                .render(str.toString());
    }
}
