package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Author;
import com.sergeybochkov.lilies.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repo;

    @Autowired
    public AuthorServiceImpl(AuthorRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Author> findAll() {
        return repo.findAll(new Sort("lastName", "firstName", "middleName"));
    }

    @Override
    public Author findOne(Long id) {
        return repo.findOne(id);
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    @Override
    public Author save(Author author) {
        if (author.getId() != null) {
            Author a = repo.findOne(author.getId());
            a.setFirstName(author.getFirstName());
            a.setLastName(author.getLastName());
            a.setMiddleName(author.getMiddleName());
            return repo.save(a);
        }
        else
            return repo.save(author);
    }
}
