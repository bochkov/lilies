package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Author;
import com.sergeybochkov.lilies.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository repo;

    @Override
    public Author getOrSave(Author author) {
        Author in = repo.findByLastNameAndFirstNameAndMiddleName(author.getLastName(), author.getFirstName(), author.getMiddleName());
        return in != null ? in : repo.save(author);
    }
}
