package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.helper.TodoDatabase;

import java.util.ArrayList;
import java.util.List;

public class TodoModel {
    private long id;
    private String username;
    private String task;
    private boolean isCompleted;

    public TodoModel(long id, String username, String task, boolean isCompleted) {
        this.id = id;
        this.username = username;
        this.task = task;
        this.isCompleted = isCompleted;
    }

    // Getter dan Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public static class TodoDAO{
        private SQLiteDatabase db;
        private TodoDatabase dbTodo;

        public TodoDAO(Context context) {
            dbTodo = new TodoDatabase(context);
        }
        public void open() {
            db = dbTodo.getWritableDatabase();
        }
        public void close() {
            dbTodo.close();
        }

        public void createTask(String username, String taskText) {
            ContentValues values = new ContentValues();
            values.put(TodoDatabase.COLUMN_USERNAME, username);
            values.put(TodoDatabase.COLUMN_TASK, taskText);

            db.insert(TodoDatabase.TABLE_TODO, null, values);
        }

        public List<TodoModel> getAllTasks(String username) {
            List<TodoModel> tasks = new ArrayList<>();
            Cursor cursor = db.query(
                    TodoDatabase.TABLE_TODO,
                    null,
                    TodoDatabase.COLUMN_USERNAME + " = ?",
                    new String[]{username},
                    null,
                    null,
                    "CASE WHEN " + TodoDatabase.COLUMN_IS_COMPLETED + " = 0 THEN 0 ELSE 1 END, " + TodoDatabase.COLUMN_ID + " DESC"
            );

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    TodoModel task = cursorToTask(cursor);
                    tasks.add(task);
                    cursor.moveToNext();
                }
                cursor.close();
            }

            return tasks;
        }

        public void updateTask(TodoModel task) {
            ContentValues values = new ContentValues();
            values.put(TodoDatabase.COLUMN_TASK, task.getTask());
            values.put(TodoDatabase.COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);

            db.update(TodoDatabase.TABLE_TODO, values, TodoDatabase.COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())});
        }

        public void deleteTask(TodoModel task) {
            db.delete(TodoDatabase.TABLE_TODO, TodoDatabase.COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())});
        }

        private TodoModel cursorToTask(Cursor cursor) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(TodoDatabase.COLUMN_ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(TodoDatabase.COLUMN_USERNAME));
            String task = cursor.getString(cursor.getColumnIndexOrThrow(TodoDatabase.COLUMN_TASK));
            boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(TodoDatabase.COLUMN_IS_COMPLETED)) > 0;

            return new TodoModel(id, username, task, isCompleted);
        }
    }
}
