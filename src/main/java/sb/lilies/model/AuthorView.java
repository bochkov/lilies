package sb.lilies.model;

import org.springframework.beans.factory.annotation.Value;

public interface AuthorView {

    Long getId();

    @Value("#{target.lastName}")
    String getName();

}
