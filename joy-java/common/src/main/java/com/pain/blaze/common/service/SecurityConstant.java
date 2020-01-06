package com.pain.blaze.common.service;

public interface SecurityConstant {

    // Public security means a user may be logged out or logged in
    int SEC_PUBLIC = 0;

    // Authenticated security means a user must be logged in
    int SEC_AUTHENTICATED = 1;

    // Admin security means a user must be both logged in and have sudo flag
    int SEC_ADMIN = 2;
}
