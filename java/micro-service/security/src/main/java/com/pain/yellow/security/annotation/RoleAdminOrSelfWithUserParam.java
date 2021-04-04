package com.pain.yellow.security.annotation;

import com.pain.yellow.security.util.Constants;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TODO anootation user admin api
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("authentication.name == #user.username or " +
        "hasAnyAuthority('" + Constants.ROLE_ADMIN + "', '" + Constants.AUTHORITY_USER_UPDATE + "')")
public @interface RoleAdminOrSelfWithUserParam {
}
