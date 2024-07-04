package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model;

public class MetaApiTodoModel {
    private int total_items;
    private int total_pages;
    private int per_page_item;
    private int current_page;
    private int page_size;
    private boolean has_more_page;
    public MetaApiTodoModel(int total_items, int total_pages, int per_page_item, int current_page, int page_size, boolean has_more_page) {
        this.total_items = total_items;
        this.total_pages = total_pages;
        this.per_page_item = per_page_item;
        this.current_page = current_page;
        this.page_size = page_size;
        this.has_more_page = has_more_page;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getPer_page_item() {
        return per_page_item;
    }

    public void setPer_page_item(int per_page_item) {
        this.per_page_item = per_page_item;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public boolean isHas_more_page() {
        return has_more_page;
    }

    public void setHas_more_page(boolean has_more_page) {
        this.has_more_page = has_more_page;
    }
}
