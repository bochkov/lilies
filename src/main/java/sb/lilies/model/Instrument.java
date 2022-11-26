package sb.lilies.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "instrument")
public final class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "instrument_id_seq")
    @Column(name = "instrument_id")
    private Integer id;

    private String name;

    @Column(unique = true)
    private String slug;

}
