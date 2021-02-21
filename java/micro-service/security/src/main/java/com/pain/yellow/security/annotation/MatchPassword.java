package com.pain.yellow.security.annotation;

import com.pain.yellow.security.validation.MatchPasswordValidator;

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
