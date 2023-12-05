package sb.lilies.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "instrument")
public final class Instrument {

    @Id
    @Column(name = "instrument_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instrument_id_gen")
    @SequenceGenerator(name = "instrument_id_gen", sequenceName = "instrument_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    @Column(unique = true)
    private String slug;

}
