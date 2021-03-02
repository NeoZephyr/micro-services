package com.pain.yellow.security.validation;

import com.pain.yellow.security.annotation.MatchPassword;
import com.pain.yellow.security.domain.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchPasswordValidator implements ConstraintValidator<MatchPassword, UserDto> {
    @Override
    public void initialize(MatchPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserDto value, ConstraintValidatorContext context) {
        return value.getPassword().equals(value.getMatchPassword());
    }
}
