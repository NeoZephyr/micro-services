package com.pain.blue.wiki.request.user;

import com.pain.blue.wiki.request.PageRequest;
import lombok.Data;

import java.util.Date;

@Data
public class UserIndexRequest extends PageRequest {
    private Long id;

    private String name;

    private String nickname;

    private String password;

    private Date dateCreated;

    private Date lastUpdated;
}
