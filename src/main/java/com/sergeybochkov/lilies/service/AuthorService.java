package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    Author findOne(Long id);

    void delete(Long id);

    Author save(Author author);
}
