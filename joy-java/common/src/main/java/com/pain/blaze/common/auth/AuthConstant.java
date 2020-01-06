package com.pain.blaze.common.auth;

public interface AuthConstant {
    String COOKIE_NAME = "joy-faraday";

    // header set for internal user id
    String CURRENT_USER_HEADER = "faraday-current-user-id";

    // AUTHORIZATION_HEADER is the http request header
    // key used for accessing the internal authorization.
    String AUTHORIZATION_HEADER = "Authorization";

    // AUTHORIZATION_ANONYMOUS_WEB is set as the Authorization header to denote that
    // a request is being made by an unauthenticated web user
    String AUTHORIZATION_ANONYMOUS_WEB = "faraday-anonymous";

    // AUTHORIZATION_COMPANY_SERVICE is set as the Authorization header to denote
    // that a request is being made by the company service
    String AUTHORIZATION_COMPANY_SERVICE = "company-service";

    // AUTHORIZATION_BOT_SERVICE is set as the Authorization header to denote that
    // a request is being made by the bot microservice
    String AUTHORIZATION_BOT_SERVICE = "bot-service";

    // AUTHORIZATION_ACCOUNT_SERVICE is set as the Authorization header to denote that
    // a request is being made by the account service
    String AUTHORIZATION_ACCOUNT_SERVICE = "account-service";

    // AUTHORIZATION_SUPPORT_USER is set as the Authorization header to denote that
    // a request is being made by a Joy team member
    String AUTHORIZATION_SUPPORT_USER = "faraday-support";

    // AUTHORIZATION_SUPERPOWERS_SERVICE is set as the Authorization header to
    // denote that a request is being made by the dev-only superpowers service
    String AUTHORIZATION_SUPERPOWERS_SERVICE = "superpowers-service";

    // AUTHORIZATION_WWW_SERVICE is set as the Authorization header to denote that
    // a request is being made by the www login / signup system
    String AUTHORIZATION_WWW_SERVICE = "www-service";

    // AUTH_WHOAMI_SERVICE is set as the Authorization heade to denote that
    // a request is being made by the whoami microservice
    String AUTHORIZATION_WHOAMI_SERVICE = "whoami-service";

    // AUTHORIZATION_AUTHENTICATED_USER is set as the Authorization header to denote that
    // a request is being made by an authenticated web user
    String AUTHORIZATION_AUTHENTICATED_USER = "faraday-authenticated";

    // AUTHORIZATION_ICAL_SERVICE is set as the Authorization header to denote that
    // a request is being made by the ical service
    String AUTHORIZATION_ICAL_SERVICE = "ical-service";

    // AUTH ERROR Messages
    String ERROR_MSG_DO_NOT_HAVE_ACCESS = "You do not have access to this service";
    String ERROR_MSG_MISSING_AUTH_HEADER = "Missing Authorization http header";
}