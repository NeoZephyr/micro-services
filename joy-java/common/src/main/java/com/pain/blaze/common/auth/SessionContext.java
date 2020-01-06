package com.pain.blaze.common.auth;

import java.util.concurrent.TimeUnit;

public class SessionContext {

    public static final long SHORT_SESSION = TimeUnit.HOURS.toMillis(12);
    public static final long LONG_SESSION = TimeUnit.HOURS.toMillis(30 * 24);

    public static void login() {}

    public static void logout() {}
}
