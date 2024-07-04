package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model;

public class ApiTodoModel {
    private String _id;
    private String title;
    private String description;
    private boolean is_completed;
    public ApiTodoModel(String id, String title, String description, boolean is_completed) {
        this._id = id;
        this.title = title;
        this.description = description;
        this.is_completed = is_completed;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }
}
