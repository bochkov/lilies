package com.sergeybochkov.lilies.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "difficulty")
public class Difficulty {

    @Id
    @Column(name = "rating")
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany
    private List<Music> musicList;

    public Difficulty() {}

    public Difficulty(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
