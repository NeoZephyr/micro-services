package com.pain.blue.wiki.request.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategorySaveRequest {

    private Long id;

    @NotBlank(message = "名称不能为空")
    private String name;

    private Long parent;

    private Integer sort;
}
