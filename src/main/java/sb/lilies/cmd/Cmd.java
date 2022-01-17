package sb.lilies.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

public final class Cmd {

    private static final Pattern HEADER = Pattern.compile(".*\\\\header\\s*\\{(?<header>.*?)}", Pattern.DOTALL);
    private static final Pattern LINE = Pattern.compile("^(?<key>.*)\\s*=\\s*\"(?<value>.*)\"$");

    private final DataSource ds;

    public Cmd(Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.user"));
        config.setPassword(properties.getProperty("db.pass"));
        this.ds = new HikariDataSource(config);
    }

    private Options cliOptions() {
        Options options = new Options();
        options.addOption(new Option("f", true, "file to parse"));
        options.addOption(new Option("d", true, "sheet difficulty"));
        options.addOption(new Option("i", true, "sheet instruments"));
        options.addOption(new Option("na", false, "no parse authors"));
        options.addOption(new Option("c", true, "composer"));
        options.addOption(new Option("w", true, "writer"));
        return options;
    }

    private Metadata parseHeaders(String content, CommandLine cli) throws IOException {
        Matcher matcher = HEADER.matcher(content);
        if (matcher.find()) {
            Map<String, String> headers = new HashMap<>();
            for (String line : matcher.group("header").split("\n")) {
                Matcher lm = LINE.matcher(line);
                if (lm.find()) {
                    headers.put(lm.group("key").trim(), lm.group("value").trim());
                }
            }
            if (cli.hasOption("na"))
                headers.put("composer", "");
            if (cli.hasOption("c"))
                headers.put("composer", cli.getOptionValue("c"));
            if (cli.hasOption("w"))
                headers.put("writer", cli.getOptionValue("w"));
            return new Metadata(
                    headers, cli.getOptionValue("i").split(","), Integer.parseInt(cli.getOptionValue("d"))
            );
        } else
            throw new IOException("Header section not found");
    }

    public void parse(String[] args) throws ParseException, IOException {
        Options options = cliOptions();
        CommandLineParser cmdLine = new DefaultParser();
        CommandLine cli = cmdLine.parse(options, args);
        if (cli.hasOption("f") && cli.hasOption("d") && cli.hasOption("i")) {
            File file = new File(
                    cli.getOptionValue("f")
                            .replaceFirst("^~", System.getProperty("user.home"))
                            .replaceFirst("^\\.", System.getProperty("user.dir"))
            );
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            Metadata metadata = parseHeaders(content, cli);
            save(file, metadata);
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
        File propFile = new File(System.getProperty("user.dir"), "lilies.properties");
        try (InputStream is = new FileInputStream(propFile)) {
            props.load(is);
        }
        new Cmd(props).parse(args);
    }
}
