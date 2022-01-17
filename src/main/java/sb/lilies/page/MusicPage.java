package sb.lilies.page;

import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ratpack.exec.Promise;
import ratpack.form.Form;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import sb.lilies.CtMusics;
import sb.lilies.FilterDifficulties;
import sb.lilies.FilterInstrument;

import javax.sql.DataSource;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public final class MusicPage implements Handler {

    private final DataSource ds;
    private final PebbleEngine pebble;

    @Override
    public void handle(Context context) {
        Promise<Form> promise = context.parse(Form.class);
        promise.then(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("object_list",
                    new FilterDifficulties(f.getAll("difficulties[]"),
                            new FilterInstrument(f.getAll("instruments[]"),
                                    new CtMusics(ds))
                    ).iterate());
            StringWriter str = new StringWriter();
            pebble.getTemplate("templates/lilies/ajax_list.html")
                    .evaluate(str, map);
            context.header("Content-Type", "text/html")
                    .render(str.toString());
        });
    }
}
