package sb.lilies.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "difficulty")
public final class Difficulty {

    @Id
    private Integer rating;

    private String name;

}
