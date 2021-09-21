package com.pain.blue.rest.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<E> {
    private int page;
    private long records;
    private long total;
    private List<E> rows;

    public PageResult() {
    }

    public PageResult(int page, long records, long total, List<E> rows) {
        this.page = page;
        this.records = records;
        this.total = total;
        this.rows = rows;
    }
}
