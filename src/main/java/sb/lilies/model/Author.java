package sb.lilies.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "author")
public final class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "author_id_seq")
    @Column(name = "author_id")
    private Long id;

    private String lastName;

    private String firstName;

    private String middleName;

}
