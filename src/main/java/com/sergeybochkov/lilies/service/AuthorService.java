package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Author;

public interface AuthorService {

    Author getOrSave(Author author);
}
