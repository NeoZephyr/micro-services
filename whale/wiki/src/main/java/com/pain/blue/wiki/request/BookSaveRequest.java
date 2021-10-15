package com.pain.blue.wiki.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class BookSaveRequest {

    @NotBlank(message = "名称不能为空")
    private String name;

    private Long categoryId;

    private Long subCategoryId;

    private String description;

    private String cover;

    private BigDecimal price;

    private Long viewCount;

    private Long voteCount;

    private Date dateCreated;

    private Date lastUpdated;
}
