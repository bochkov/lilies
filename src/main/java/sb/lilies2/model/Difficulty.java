package sb.lilies2.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "difficulty")
public final class Difficulty {

    @Id
    private Integer rating;

    private String name;

}
