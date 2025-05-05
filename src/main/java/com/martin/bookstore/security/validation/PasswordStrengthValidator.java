package com.martin.bookstore.security.validation;

import com.martin.bookstore.security.properties.PasswordValidationProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordStrengthValidator implements ConstraintValidator<StrongPassword, String> {

    private final PasswordValidationProperties properties;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return password.matches(properties.getPattern());
    }
}