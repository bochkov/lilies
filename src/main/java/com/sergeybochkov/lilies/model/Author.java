package com.sergeybochkov.lilies.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "author")
public final class Author implements Serializable {

    @Id
    @Column(name = "author_id")
    @GeneratedValue(generator = "author_seq_generator")
    @GenericGenerator(name = "author_seq_generator", strategy = "increment")
    private Long id;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "middle_name")
    private String middlename;

    @ManyToMany
    @JoinTable(name = "author_music",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id"))
    private List<Music> musicList;

    public Author() {
    }

    public Author(Long id, String lastname, String firstname, String middlename) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastname;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getMiddleName() {
        return middlename;
    }

    public String print() {
        return String.format("%s %s %s", lastname, firstname, middlename);
    }
}
