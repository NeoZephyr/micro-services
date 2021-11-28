package com.pain.blue.wiki.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    // 防止精度丢失
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private String nickname;

    private Date dateCreated;

    private Date lastUpdated;
}
