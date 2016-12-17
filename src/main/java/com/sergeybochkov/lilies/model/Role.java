package com.sergeybochkov.lilies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public final class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(generator = "role_seq_generator")
    @GenericGenerator(name = "role_seq_generator", strategy = "increment")
    private Long id;
    private String role;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {}

    public Role(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
