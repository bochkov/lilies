package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.User;
import com.sergeybochkov.lilies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DbUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails loadedUser;
        try {
            User client = repo.findByUsername(s);
            loadedUser = new org.springframework.security.core.userdetails.User(client.getUsername(),
                    client.getPassword(), client.getRoles());
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
        return loadedUser;
    }
}
