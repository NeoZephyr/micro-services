package com.pain.yellow.app.annotation.validator;

import com.pain.yellow.app.util.validator.MatchPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchPasswordValidator.class)
@Documented
public @interface MatchPassword {
    String message() default "{PasswordMatches.userDto}";;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
