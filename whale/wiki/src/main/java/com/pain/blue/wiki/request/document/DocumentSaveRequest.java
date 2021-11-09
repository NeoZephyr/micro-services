package com.pain.blue.wiki.request.document;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DocumentSaveRequest {

    private Long id;

    @NotNull(message = "书不能为空")
    private Long bookId;

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotNull(message = "父文档不能为空")
    private Long parent;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;
}
