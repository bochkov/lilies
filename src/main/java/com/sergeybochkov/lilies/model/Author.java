package com.sergeybochkov.lilies.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "author")
public class Author implements Serializable {

    @Id
    @Column(name = "author_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq_generator")
    @SequenceGenerator(name = "author_seq_generator", sequenceName = "author_sequence")
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

    public Author() {}

    public Author(String lastName, String firstName, String middleName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", lastName, firstName, middleName);
    }

    public String str() {
        return "Author {" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                '}';
    }
}
