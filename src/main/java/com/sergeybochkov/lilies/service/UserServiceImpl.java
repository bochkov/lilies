package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.User;
import com.sergeybochkov.lilies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public final class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository repo;

    @Autowired
    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User client = repo.findByUsername(s);
        if (client == null)
            throw new UsernameNotFoundException(String.format("User %s not found", s));
        return new org.springframework.security.core.userdetails.User(client.getUsername(),
                client.getPassword(), client.getRoles());
    }

    @Override
    public User save(User user) {
        User u = repo.findByUsername(user.getUsername());
        return u != null ?
                repo.save(new User(u.getId(), u.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()))) :
                null;
    }
}
