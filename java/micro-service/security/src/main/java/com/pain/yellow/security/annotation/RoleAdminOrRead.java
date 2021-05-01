package com.pain.yellow.security.annotation;

import com.pain.yellow.security.util.Constants;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('" +
        Constants.AUTHORITY_ADMIN +
        "', '" +
        Constants.AUTHORITY_USER_READ +
        "')")
public @interface RoleAdminOrRead {
}
