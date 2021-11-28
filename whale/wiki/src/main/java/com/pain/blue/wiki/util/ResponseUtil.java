package com.pain.blue.wiki.util;

import com.pain.blue.rest.response.ResponseStatus;

public class ResponseUtil {

    public static final ResponseStatus USER_EXIST = new ResponseStatus(1, "user already exist");
    public static final ResponseStatus USER_NOT_EXIST = new ResponseStatus(2, "user not exist");
}
