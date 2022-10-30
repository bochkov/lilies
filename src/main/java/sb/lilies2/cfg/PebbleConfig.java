package sb.lilies2.cfg;

import com.mitchellbosecke.pebble.extension.Extension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sb.lilies2.cfg.pebble.LiliesExtension;

@Configuration
public class PebbleConfig {

    @Bean
    public Extension liliesExtension() {
        return new LiliesExtension(5);
    }
}
