package sb.lilies;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ratpack.error.ClientErrorHandler;
import ratpack.error.ServerErrorHandler;
import ratpack.registry.Registry;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import sb.lilies.page.*;
import sb.lilies.pebble.LiliesExtension;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class App {

    private final DataSource ds;
    private final PebbleEngine pebble;

    public App(DataSource ds, PebbleEngine pebble) {
        this.ds = ds;
        this.pebble = pebble;
    }

    public void start() throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(config -> {
                    config.baseDir(BaseDir.find());
                    config.development(false);
                })
                .registry(reg -> reg.join(
                        Registry.builder()
                                .add(ServerErrorHandler.class, new ServerErrorPage(pebble))
                                .add(ClientErrorHandler.class, new ClientErrorPage(pebble))
                                .build()))
                .handlers(chain -> chain
                        .files(f -> f.files("static"))
                        .get("",
                                new IndexPage(ds, pebble))
                        .get("about",
                                new AboutPage(ds, pebble))
                        .get("sheet/:id/",
                                new DetailPage(ds, pebble))
                        .get("search/",
                                new SearchPage(ds, pebble))
                        .post("a/music/",
                                new MusicPage(ds, pebble))
                        .notFound()
                )
        );
    }

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (InputStream fis = new FileInputStream(
                new File(
                        System.getProperty("user.dir"),
                        "lilies.properties"))) {
            props.load(fis);
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.user"));
        config.setPassword(props.getProperty("db.pass"));
        DataSource ds = new HikariDataSource(config);
        PebbleEngine pebble = new PebbleEngine.Builder()
                .loader(new ClasspathLoader())
                .extension(new LiliesExtension((int) new PgDifficulties(ds).count()))
                .build();
        new App(ds, pebble).start();
    }
}
