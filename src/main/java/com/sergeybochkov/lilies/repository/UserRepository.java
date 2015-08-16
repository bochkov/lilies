package com.sergeybochkov.lilies.repository;

import com.sergeybochkov.lilies.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
