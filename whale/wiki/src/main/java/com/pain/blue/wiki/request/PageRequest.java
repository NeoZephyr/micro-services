package com.pain.blue.wiki.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class PageRequest {

    @NotNull(message = "页面不能为空")
    private int page = 1;

    @NotNull(message = "每页条数不能为空")
    @Max(value = 100, message = "每页条数不能超过 100")
    private int size = 10;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}
