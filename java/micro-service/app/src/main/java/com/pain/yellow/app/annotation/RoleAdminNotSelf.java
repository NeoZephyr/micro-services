package com.pain.yellow.app.annotation;

import com.pain.yellow.security.util.Constants;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') and " +
        "authentication.name != #username")
public @interface RoleAdminNotSelf {
}
