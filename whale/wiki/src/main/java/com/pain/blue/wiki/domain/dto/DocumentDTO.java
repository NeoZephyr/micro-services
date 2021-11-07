package com.pain.blue.wiki.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class DocumentDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long bookId;

    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long parent;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

    private Date dateCreated;

    private Date lastUpdated;
}
