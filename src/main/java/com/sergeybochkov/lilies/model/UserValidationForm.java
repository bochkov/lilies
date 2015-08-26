package com.sergeybochkov.lilies.model;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidationForm implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        if (!user.getPassword().equals(user.getPasswordRepeat()))
            errors.rejectValue("password", "user.error.password.no_match");
    }
}
