package com.pain.blaze.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.matches("[0-9]+") && value.length() > 8 && value.length() < 14) {
            return true;
        }

        return false;
    }
}
