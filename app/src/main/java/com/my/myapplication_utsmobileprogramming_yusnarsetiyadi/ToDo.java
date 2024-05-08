package com.my.myapplication_utsmobileprogramming_yusnarsetiyadi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

public class ToDo extends AppCompatActivity {
    private Button addButton;
    private LinearLayout taskListLayout;
    private SharedPreferences sharedPreferences;
    private TextView textGuide;
    private TextView selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        textGuide = findViewById(R.id.guide);
        textGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ToDo.this);
                builder.setMessage
                        ("Button Add to add, Click task to edit, and Hold task to delete.").setNegativeButton
                        ("ok", null).create().show();
            }
        });

        addButton = findViewById(R.id.add_button);
        taskListLayout = findViewById(R.id.task_list_layout);
        sharedPreferences = getSharedPreferences("MyTasks", Context.MODE_PRIVATE);

        loadTasks();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveTasks();
        Intent intent = new Intent(ToDo.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void showAddTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
        EditText taskInput = dialogView.findViewById(R.id.task_input);
        Button saveButton = dialogView.findViewById(R.id.save_button);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(dialogView);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = taskInput.getText().toString().trim();
                if (!task.isEmpty()) {
                    if (selectedTask != null) {
                        // Update task
                        selectedTask.setText(task);
                        selectedTask = null; // Reset selectedTask
                    } else {
                        // Add new task
                        addTask(task);
                    }
                    dialog.dismiss(); // Close the dialog
                } else {
                    Toast.makeText(ToDo.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addTask(String task) {
        TextView textView = new TextView(this);
        textView.setText(task);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTask = (TextView) v;
                showAddTaskDialog();
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                taskListLayout.removeView(v);
                saveTasks();
                return true;
            }
        });
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        Drawable leftDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_task_alt_24);
        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());
            textView.setCompoundDrawables(leftDrawable, null, null, null);
            textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.drawable_padding));
        }
        textView.setTextColor(Color.WHITE);
        taskListLayout.addView(textView);

        saveTasks();
    }

    private void saveTasks() {
        Set<String> taskSet = new HashSet<>();
        for (int i = 0; i < taskListLayout.getChildCount(); i++) {
            View view = taskListLayout.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                taskSet.add(textView.getText().toString());
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("tasks", taskSet);
        editor.apply();
    }

    private void loadTasks() {
        Set<String> taskSet = sharedPreferences.getStringSet("tasks", new HashSet<String>());
        for (String task : taskSet) {
            TextView textView = new TextView(this);
            textView.setText(task);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTask = (TextView) v;
                    showAddTaskDialog();
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    taskListLayout.removeView(v);
                    saveTasks();
                    return true;
                }
            });
            textView.setTextSize(20);
            textView.setTypeface(null, Typeface.BOLD);
            Drawable leftDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_task_alt_24);
            if (leftDrawable != null) {
                leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());
                textView.setCompoundDrawables(leftDrawable, null, null, null);
                textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.drawable_padding));
            }
            textView.setTextColor(Color.WHITE);
            taskListLayout.addView(textView);
        }
    }
}