package com.epam.esm.service;

import java.util.List;

public class Pagination<E> {
    private final int totalCount;
    private final int page;
    private final int perPage;

    private final List<E> items;

    public Pagination(int totalCount, int page, int perPage, List<E> items) {
        this.totalCount = totalCount;
        this.page = page;
        this.perPage = perPage;
        this.items = items;
    }

    public int getPagesCount() {
        return (int) Math.ceil(this.totalCount / this.perPage);
    }

    public int getOffset() {
        return (this.page - 1) * this.perPage;
    }

    public int getLimit() {
        return perPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }

    public List<E> getItems() {
        return items;
    }
}
