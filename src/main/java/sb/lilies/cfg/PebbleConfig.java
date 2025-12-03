package sb.lilies.cfg;

import io.pebbletemplates.pebble.extension.Extension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sb.lilies.cfg.pebble.LiliesExtension;

@Configuration
public class PebbleConfig {

    @Bean
    public Extension liliesExtension() {
        return new LiliesExtension();
    }
}
