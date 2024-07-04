package com.homedelivery.validation;

import com.homedelivery.model.annotation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailConstraintValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        if (email != null && !email.isBlank()) {

            String regex = "^[a-zA-Z0-9]+[_.]?[a-zA-Z0-9]+@[a-zA-Z]+.+[a-zA-Z]+$";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);

            return matcher.matches();
        }

        return false;
    }
}