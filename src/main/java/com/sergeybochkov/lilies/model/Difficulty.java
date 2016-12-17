package com.sergeybochkov.lilies.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "difficulty")
public final class Difficulty implements Serializable {

    @Id
    @Column(name = "rating")
    private Integer rating;

    @Column(name = "name")
    private String name;

    @OneToMany
    @JoinTable(name = "difficulty_music",
            joinColumns = @JoinColumn(name = "rating"),
            inverseJoinColumns = @JoinColumn(name = "music_id"))
    private List<Music> musicList;

    public Difficulty() {}

    public Difficulty(Integer rating, String name) {
        this.rating = rating;
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String print() {
        return String.format("%s - %s", this.rating, this.name);
    }
}
