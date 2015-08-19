package com.sergeybochkov.lilies.repository;

import com.sergeybochkov.lilies.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
