package sb.lilies.page;

import com.mitchellbosecke.pebble.PebbleEngine;
import ratpack.handling.Context;
import sb.lilies.PgMusic;

import javax.sql.DataSource;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class DetailPage implements Page {

    private final DataSource ds;
    private final PebbleEngine pebble;

    public DetailPage(DataSource ds, PebbleEngine pebble) {
        this.ds = ds;
        this.pebble = pebble;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("object", new PgMusic(ds, ctx.getAllPathTokens().asInt("id")));
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
