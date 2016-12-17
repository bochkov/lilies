package com.sergeybochkov.lilies.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public final class CurrentUserControllerAdvice {

    @ModelAttribute("currentUser")
    public UserDetails getCurrentUser(Authentication auth) {
        return (auth == null) ? null : (UserDetails) auth.getPrincipal();
    }
}
