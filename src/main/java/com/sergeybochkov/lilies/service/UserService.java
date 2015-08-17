package com.sergeybochkov.lilies.service;

import com.sergeybochkov.lilies.model.User;

public interface UserService {

    User saveOrUpdate(User user);
}
