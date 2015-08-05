package com.sergeybochkov.lilies.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "instrument")
public class Instrument implements Serializable {

    @Id
    @GeneratedValue(generator = "instrument_seq_generator")
    @Column(name = "instrument_id")
    @SequenceGenerator(name = "instrument_seq_generator", sequenceName = "instrument_sequence")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany
    private List<Music> musicList;

    public Instrument() {}

    public Instrument(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Instrument {" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
