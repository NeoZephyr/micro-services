package com.pain.blue.wiki.util;

import com.pain.blue.rest.response.ResponseStatus;

public class ResponseUtil {

    public static final ResponseStatus USER_EXIST = new ResponseStatus(1, "用户已存在");
    public static final ResponseStatus USER_NOT_EXIST = new ResponseStatus(2, "用户不存在");
    public static final ResponseStatus USER_LOGIN_FAILED = new ResponseStatus(3, "用户登录失败");
}
