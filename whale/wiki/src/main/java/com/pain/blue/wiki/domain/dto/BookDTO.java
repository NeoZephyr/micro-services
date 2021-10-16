package com.pain.blue.wiki.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BookDTO {

    // 防止精度丢失
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long subCategoryId;

    private String description;

    private String cover;

    private BigDecimal price;

    private Long viewCount;

    private Long voteCount;

    private Date dateCreated;

    private Date lastUpdated;
}
