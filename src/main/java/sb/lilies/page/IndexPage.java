package sb.lilies.page;

import com.mitchellbosecke.pebble.PebbleEngine;
import ratpack.handling.Context;
import sb.lilies.CtDifficulties;
import sb.lilies.CtInstruments;

import javax.sql.DataSource;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class IndexPage implements Page {

    private final DataSource ds;
    private final PebbleEngine pebble;

    public IndexPage(DataSource ds, PebbleEngine pebble) {
        this.ds = ds;
        this.pebble = pebble;
    }

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
