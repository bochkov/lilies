package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.Author;
import com.sergeybochkov.lilies.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository repo;

    @Override
    public Author getOrSave(Author author) {
        Author in = repo.findByLastNameAndFirstNameAndMiddleName(author.getLastName(), author.getFirstName(), author.getMiddleName());
        return in != null ? in : repo.save(author);
    }

    @Override
    public List<Author> findAll() {
        return repo.findAll(new Sort("lastName", "firstName", "middleName"));
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }
}
