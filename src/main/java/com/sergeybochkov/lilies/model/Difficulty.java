package com.sergeybochkov.lilies.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "difficulty")
public class Difficulty {

    @Id
    @Column(name = "rating")
    private Integer rating;

    @Column(name = "name")
    private String name;

    @OneToMany
    private List<Music> musicList;

    public Difficulty() {}

    public Difficulty(Integer rating, String name) {
        this.rating = rating;
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
