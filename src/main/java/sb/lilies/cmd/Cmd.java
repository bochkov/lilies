package sb.lilies.cmd;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Cmd {

    private final static Pattern HEADER = Pattern.compile(".*\\\\header\\s*\\{(?<header>.*?)}", Pattern.DOTALL);
    private final static Pattern LINE = Pattern.compile("^(?<key>.*)\\s*=\\s*\"(?<value>.*)\"$");

    private final DataSource ds;

    public Cmd(Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.user"));
        config.setPassword(properties.getProperty("db.pass"));
        this.ds = new HikariDataSource(config);
    }

    public void parse(String[] args) throws Exception {
        Options options = new Options();
        options.addOption(new Option("f", true, "file to parse"));
        options.addOption(new Option("d", true, "sheet difficulty"));
        options.addOption(new Option("i", true, "sheet instruments"));
        options.addOption(new Option("na", false, "no parse authors"));
        options.addOption(new Option("c", true, "composer"));
        options.addOption(new Option("w", true, "writer"));
        CommandLineParser cmdLine = new DefaultParser();
        CommandLine cli = cmdLine.parse(options, args);
        if (cli.hasOption("f") && cli.hasOption("d") && cli.hasOption("i")) {
            File file = new File(
                    cli.getOptionValue("f")
                            .replaceFirst("^~", System.getProperty("user.home"))
                            .replaceFirst("^\\.", System.getProperty("user.dir")));
            String fstr = FileUtils.readFileToString(file, "UTF-8");
            Matcher matcher = HEADER.matcher(fstr);
            if (matcher.find()) {
                String title = "", subtitle = "", composer = "", writer = "";
                for (String line : matcher.group("header").split("\n")) {
                    Matcher lm = LINE.matcher(line);
                    if (lm.find()) {
                        String key = lm.group("key").trim();
                        String value = lm.group("value").trim();
                        switch (key) {
                            case "title":
                                title = value;
                                break;
                            case "subtitle":
                                subtitle = value;
                                break;
                            case "composer":
                                composer = value;
                                break;
                        }
                    }
                }
                if (cli.hasOption("na"))
                    composer = "";
                if (cli.hasOption("c"))
                    composer = cli.getOptionValue("c");
                if (cli.hasOption("w"))
                    writer = cli.getOptionValue("w");
                save(file, new Metadata(
                        title,
                        subtitle,
                        composer,
                        writer,
                        cli.getOptionValue("i").split(","),
                        Integer.parseInt(cli.getOptionValue("d"))));
            } else
                throw new IOException("Header section not found");
        } else
            new HelpFormatter().printHelp("cmd", options);
    }

    private void save(File file, Metadata md) {
        new Ask(md, ds,
                new Save(md, ds,
                        new Src(file))
        ).act();
    }

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(
                new File(
                        System.getProperty("user.dir"),
                        "lilies.properties"))) {
            props.load(is);
        }
        new Cmd(props).parse(args);
    }
}
