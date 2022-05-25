package sb.lilies;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ratpack.core.error.ClientErrorHandler;
import ratpack.core.error.ServerErrorHandler;
import ratpack.core.server.BaseDir;
import ratpack.core.server.RatpackServer;
import ratpack.exec.registry.Registry;
import sb.lilies.app.MediaFiles;
import sb.lilies.app.StaticFiles;
import sb.lilies.page.*;
import sb.lilies.pebble.LiliesExtension;

@Slf4j
@RequiredArgsConstructor
public final class App {

    private final DataSource ds;
    private final PebbleEngine pebble;
    private final boolean develop;

    public void start() throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(config -> {
                    config.development(develop);
                    config.baseDir(develop ?
                            new File(System.getProperty("user.dir")).toPath() :
                            BaseDir.find()
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
            if (arg.equals("develop")) {
                develop = true;
                break;
            }
        }
        Properties props = new Properties();
        File propFile = new File(System.getProperty("user.dir"), "lilies.properties");
        try (InputStream fis = new FileInputStream(propFile)) {
            props.load(fis);
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.user"));
        config.setPassword(props.getProperty("db.pass"));
        DataSource ds = new HikariDataSource(config);
        PebbleEngine pebble = new PebbleEngine.Builder()
                .loader(new ClasspathLoader())
                .extension(new LiliesExtension(new PgDifficulties(ds).maxValue()))
                .build();
        LOG.info("Starting with params: develop={}", develop);
        new App(ds, pebble, develop).start();
    }

}
