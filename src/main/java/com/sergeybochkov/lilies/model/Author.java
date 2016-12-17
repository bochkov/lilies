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
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @ManyToMany
    @JoinTable(name = "author_music",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id"))
    private List<Music> musicList;

    public Author() {
    }

    public Author(String lastName, String firstName, String middleName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String print() {
        return String.format("%s %s %s", lastName, firstName, middleName);
    }
}
