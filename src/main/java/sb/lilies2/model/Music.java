package sb.lilies2.model;

import java.util.Set;
import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "music")
public final class Music implements Comparable<Music> {

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

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
//    @PrimaryKeyJoinColumn
//    private Storage storage;

    @Override
    public int compareTo(Music o) {
        return getName().compareTo(o.getName());
    }

    public boolean matched(String query) {
        String token = query.toLowerCase();
        return name.toLowerCase().contains(token) ||
                subname.toLowerCase().contains(token) ||
                composers.stream().anyMatch(p -> p.getLastName().toLowerCase().contains(token)) ||
                writers.stream().anyMatch(p -> p.getLastName().toLowerCase().contains(token));
    }

}
