package com.pain.blue.wiki.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class UserLoginDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private String nickname;

    private String token;

    private Date dateCreated;

    private Date lastUpdated;
}
