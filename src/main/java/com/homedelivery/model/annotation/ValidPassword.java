package com.homedelivery.model.annotation;

import com.homedelivery.validation.PasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface ValidPassword {
    String message() default "Password must contains at least one upper case letter, lower case letter and one digit.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}