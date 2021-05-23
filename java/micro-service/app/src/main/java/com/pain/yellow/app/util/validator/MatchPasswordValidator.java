package com.pain.yellow.app.util.validator;

import com.pain.yellow.app.annotation.validator.MatchPassword;
import com.pain.yellow.app.domain.dto.UserDto;

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
