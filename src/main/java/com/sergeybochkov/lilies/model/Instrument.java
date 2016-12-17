package com.sergeybochkov.lilies.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "instrument")
public final class Instrument implements Serializable {

    @Id
    @Column(name = "instrument_id")
    @GeneratedValue(generator = "instrument_seq_generator")
    @GenericGenerator(name = "instrument_seq_generator", strategy = "increment")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug", unique = true)
    private String slug;

    @ManyToMany
    @JoinTable(name = "instrument_music",
            joinColumns = @JoinColumn(name = "instrument_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id"))
    private List<Music> musicList;

    public Instrument() {}

    public Instrument(Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String print() {
        return name;
    }
}
