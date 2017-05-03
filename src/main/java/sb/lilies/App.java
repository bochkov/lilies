package sb.lilies;

import com.jcabi.log.Logger;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ratpack.error.ClientErrorHandler;
import ratpack.error.ServerErrorHandler;
import ratpack.registry.Registry;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import sb.lilies.app.MediaFiles;
import sb.lilies.app.StaticFiles;
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
    private final boolean develop;

    public App(DataSource ds, PebbleEngine pebble, boolean develop) {
        this.ds = ds;
        this.pebble = pebble;
        this.develop = develop;
    }

    public void start() throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(config -> {
                    config.development(develop);
                    config.baseDir(develop ?
                            new File(System.getProperty("user.dir")) :
                            BaseDir.find().toFile()
                    );
                })
                .registry(reg -> {
                    if (develop) {
                        return reg;
                    } else
                        return reg.join(
                                Registry.builder()
                                        .add(ServerErrorHandler.class, new ServerErrorPage(pebble))
                                        .add(ClientErrorHandler.class, new ClientErrorPage(pebble))
                                        .build());
                })
                .handlers(chain -> chain
                        .files(new MediaFiles(develop))
                        .files(new StaticFiles(develop))
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
        boolean develop = false;
        for (String arg : args) {
            if (arg.equals("develop"))
                develop = true;
        }
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
        Logger.info(App.class, "Starting with params {develop=%s}", develop);
        new App(ds, pebble, develop).start();
    }

}
