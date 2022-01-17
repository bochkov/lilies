package sb.lilies.page;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.RequiredArgsConstructor;
import ratpack.handling.Context;
import sb.lilies.PgMusics;

@RequiredArgsConstructor
public final class SearchPage implements Page {

    private final DataSource ds;
    private final PebbleEngine pebble;

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
