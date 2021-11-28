package com.pain.blue.wiki.request.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class UserSaveRequest {

    @NotBlank(message = "名称不能为空")
    private String name;

    private String nickname;

    private Date dateCreated;

    private Date lastUpdated;
}
