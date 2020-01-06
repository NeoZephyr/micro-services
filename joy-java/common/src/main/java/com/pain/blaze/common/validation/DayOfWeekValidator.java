package com.pain.blaze.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class DayOfWeekValidator implements ConstraintValidator<DayOfWeek, String> {

    private List<String> daysOfWeek = Arrays
            .asList("sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        String inputDay = value.trim().toLowerCase();
        if (daysOfWeek.contains(inputDay)) {
            return true;
        }

        return false;
    }
}
