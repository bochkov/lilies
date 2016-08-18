package com.sergeybochkov.lilies.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "instrument")
public class Instrument implements Serializable {

    @Id
    @Column(name = "instrument_id")
    @GeneratedValue(generator = "instrument_seq_generator")
    @SequenceGenerator(name = "instrument_seq_generator", sequenceName = "instrument_sequence", allocationSize = 5)
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

    public Instrument(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return name;
    }

    public String str() {
        return "Instrument {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
