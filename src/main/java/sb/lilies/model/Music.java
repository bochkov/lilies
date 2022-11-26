package sb.lilies.model;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "music")
public final class Music {

    @Id
    @Column(name = "music_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "music_id_seq")
    private Long id;

    private String name;

    private String subname;

    @ManyToOne
    @JoinColumn(name = "difficulty", referencedColumnName = "rating")
    private Difficulty difficulty;

    @ManyToMany
    @JoinTable(name = "music_instrument", joinColumns = @JoinColumn(name = "music_id"), inverseJoinColumns = @JoinColumn(name = "instrument_id"))
    private Set<Instrument> instruments;

    @ManyToMany
    @JoinTable(name = "music_composer", joinColumns = @JoinColumn(name = "music_id"), inverseJoinColumns = @JoinColumn(name = "composer_id"))
    private Set<Author> composers;

    @ManyToMany
    @JoinTable(name = "music_writer", joinColumns = @JoinColumn(name = "music_id"), inverseJoinColumns = @JoinColumn(name = "writer_id"))
    private Set<Author> writers;

    @Column(name = "storage_id")
    private Long storage;

}
