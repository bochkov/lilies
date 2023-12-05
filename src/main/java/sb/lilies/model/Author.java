package sb.lilies.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "author")
public final class Author {

    @Id
    @Column(name = "author_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_gen")
    @SequenceGenerator(name = "author_id_gen", sequenceName = "author_id_seq", allocationSize = 1)
    private Long id;

    private String lastName;

    private String firstName;

    private String middleName;

}
