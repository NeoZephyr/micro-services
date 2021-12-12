package com.pain.blue.wiki.request.user;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String name;
    private String password;
}
