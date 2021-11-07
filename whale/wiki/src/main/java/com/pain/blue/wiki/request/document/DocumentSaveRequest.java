package com.pain.blue.wiki.request.document;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DocumentSaveRequest {

    private Long id;

    private Long bookId;

    @NotBlank(message = "名称不能为空")
    private String name;

    private Long parent;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;
}
