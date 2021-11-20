package com.pain.blue.wiki.request.document;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DocumentUpdateRequest {
    private Long id;

    private Long bookId;

    @NotBlank(message = "名称不能为空")
    private String name;

    private Long parent;

    @NotNull(message = "内容不能为空")
    private String content;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;
}
