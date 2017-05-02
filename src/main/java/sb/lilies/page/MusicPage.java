package sb.lilies.page;

import com.mitchellbosecke.pebble.PebbleEngine;
import ratpack.exec.Promise;
import ratpack.form.Form;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import sb.lilies.FilterDifficulties;
import sb.lilies.FilterInstrument;
import sb.lilies.PgMusics;

import javax.sql.DataSource;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class MusicPage implements Handler {

    private final DataSource ds;
    private final PebbleEngine pebble;

    public MusicPage(DataSource ds, PebbleEngine pebble) {
        this.ds = ds;
        this.pebble = pebble;
    }

    @Override
    public void handle(Context context) throws Exception {
        Promise<Form> promise = context.parse(Form.class);
        promise.then(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("object_list",
                    new FilterDifficulties(f.getAll("difficulties[]"),
                            new FilterInstrument(f.getAll("instruments[]"),
                                    new PgMusics(ds))
                    ).iterate());
            StringWriter str = new StringWriter();
            pebble.getTemplate("templates/lilies/ajax_list.html")
                    .evaluate(str, map);
            context.header("Content-Type", "text/html")
                    .render(str.toString());
        });
    }
}
