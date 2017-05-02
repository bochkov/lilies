package sb.lilies.page;

import com.mitchellbosecke.pebble.PebbleEngine;
import ratpack.handling.Context;
import sb.lilies.PgMusics;

import javax.sql.DataSource;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class SearchPage implements Page {

    private final DataSource ds;
    private final PebbleEngine pebble;

    public SearchPage(DataSource ds, PebbleEngine pebble) {
        this.ds = ds;
        this.pebble = pebble;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        String query = ctx.getRequest().getQueryParams().get("query");
        Map<String, Object> map = new HashMap<>();
        map.put("query", query);
        map.put("object_list", new PgMusics(ds).search(query));
        StringWriter out = new StringWriter();
        pebble.getTemplate("templates/lilies/search.html")
                .evaluate(out, map);
        ctx.header("Content-Type", "text/html")
                .render(out.toString());
    }
}
