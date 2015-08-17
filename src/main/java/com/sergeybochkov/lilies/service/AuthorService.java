package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Author;

import java.util.List;

public interface AuthorService {

    Author getOrSave(Author author);

    List<Author> findAll();

    Author findOne(Long id);

    void delete(Long id);
}
