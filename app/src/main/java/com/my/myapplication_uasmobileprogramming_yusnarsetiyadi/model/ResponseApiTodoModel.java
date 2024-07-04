package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model;

public class ResponseApiTodoModel {
    private ApiTodoModel data;
    public ResponseApiTodoModel(ApiTodoModel data) {
        this.data = data;
    }

    public ApiTodoModel getData() {
        return data;
    }

    public void setData(ApiTodoModel data) {
        this.data = data;
    }
}
