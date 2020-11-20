package com.soft1851.utils;

import java.util.List;

/**
 * @author crq
 */
public class PageGridResult {
    /**
     * 当前页
     */
    private int page;
    /**
     * 总页数
     */
    private long total;
    /**
     * 总记录数
     */
    private long records;
    /**
     * 等待显示的内容
     */
    private List<?> rows;

    public PageGridResult(int page, long total, long records, List<?> rows) {
        this.page = page;
        this.total = total;
        this.records = records;
        this.rows = rows;
    }

    public PageGridResult() {

    }

    public PageGridResult setPage(int page) {
        this.page = page;
        return this;
    }

    public PageGridResult setTotal(long total) {
        this.total = total;
        return this;
    }

    public PageGridResult setRecords(long records) {
        this.records = records;
        return this;
    }

    public PageGridResult setRows(List<?> rows) {
        this.rows = rows;
        return this;
    }

    public int getPage() {
        return page;
    }

    public long getTotal() {
        return total;
    }

    public long getRecords() {
        return records;
    }

    public List<?> getRows() {
        return rows;
    }
}
