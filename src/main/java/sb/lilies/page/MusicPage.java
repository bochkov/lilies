package sb.lilies.page;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ratpack.core.form.Form;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;
import ratpack.exec.Promise;
import sb.lilies.CtMusics;
import sb.lilies.FilterDifficulties;
import sb.lilies.FilterInstrument;

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
