package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model;

import java.util.List;

public class ResponseListApiTodoModel {
    private List<ApiTodoModel> items;
    private MetaApiTodoModel meta;

    public ResponseListApiTodoModel(List<ApiTodoModel> items, MetaApiTodoModel meta) {
        this.items = items;
        this.meta = meta;
    }

    public List<ApiTodoModel> getItems() {
        return items;
    }

    public void setItems(List<ApiTodoModel> items) {
        this.items = items;
    }

    public MetaApiTodoModel getMeta() {
        return meta;
    }

    public void setMeta(MetaApiTodoModel meta) {
        this.meta = meta;
    }
}
