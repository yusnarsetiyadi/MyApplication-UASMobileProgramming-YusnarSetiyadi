package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.api.ApiConfigNstack;
import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.ApiTodoModel;
import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.ResponseApiTodoModel;
import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.ResponseListApiTodoModel;
import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.TodoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTodos extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private String username,id,name;
    private TextView textAbout, textDownloadData, textRefreshData;
    private Button guideButton;
    private LinearLayout taskListLayout;
    private TodoModel.TodoDAO todoDAO;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_todos);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        id = sharedPreferences.getString("id","");
        username = sharedPreferences.getString("username", "");
        name = sharedPreferences.getString("name","");
        TextView usernameTextView = findViewById(R.id.tekshome);
        usernameTextView.setText("Hello, " + name);

        textAbout = findViewById(R.id.textAbout);
        textAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTodos.this, About.class);
                MyTodos.this.startActivity(intent);
                finish();
            }
        });

        guideButton = findViewById(R.id.guide);
        guideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyTodos.this);
                builder.setTitle("How to use this ToDo?");
                builder.setMessage
                        ("This page displays data from the API, click the download button to import Todos that have been synced.").setNegativeButton
                        ("ok", null).create().show();
            }
        });

        textRefreshData = findViewById(R.id.refreshData);
        textRefreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTasks();
            }
        });

        textDownloadData = findViewById(R.id.syncData);
        textDownloadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDataAPI();
            }
        });

        taskListLayout = findViewById(R.id.task_list_layout);

        todoDAO = new TodoModel.TodoDAO(this);
        todoDAO.open();

        loadTasks();
    }

    private void loadTasks() {
        taskListLayout.removeAllViews();
        ApiConfigNstack.getRetrofitClient().getAllTodos(1,10).enqueue(new Callback<ResponseListApiTodoModel>() {
            @Override
            public void onResponse(Call<ResponseListApiTodoModel> call, Response<ResponseListApiTodoModel> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    ApiConfigNstack.getRetrofitClient().getAllTodos(1,response.body().getMeta().getTotal_items()).enqueue(new Callback<ResponseListApiTodoModel>() {
                        @Override
                        public void onResponse(Call<ResponseListApiTodoModel> call, Response<ResponseListApiTodoModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                for(ApiTodoModel apiTodoModel : response.body().getItems()){
                                    if (apiTodoModel.getTitle().contains(username)){
                                        addTaskView(apiTodoModel);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseListApiTodoModel> call, Throwable throwable) {
                            Log.e("Home", "onFailure getAllTodos: ", throwable);
                            Toast.makeText(getApplicationContext(), "There is an error or connection lost. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseListApiTodoModel> call, Throwable throwable) {
                Log.e("MyTodos", "onFailure getAllTodos: ", throwable);
                Toast.makeText(getApplicationContext(), "There is an error or connection lost. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addTaskView(final ApiTodoModel task) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        final TextView taskTextView = new TextView(this);
        taskTextView.setText(task.getDescription());
        taskTextView.setTextSize(20);
        taskTextView.setTypeface(null, Typeface.BOLD);
        Drawable leftDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_task_alt_24);
        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());
            taskTextView.setCompoundDrawables(leftDrawable, null, null, null);
            taskTextView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.drawable_padding));
        }
        taskTextView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        textParams.bottomMargin = 50;
        taskTextView.setLayoutParams(textParams);
        taskTextView.setTag(task.getId());
        if (task.isIs_completed()) {
            taskTextView.setPaintFlags(taskTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskTextView.setTextColor(Color.GRAY);
        }

        CheckBox checkBox = new CheckBox(this);
        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        checkBoxParams.setMargins(getResources().getDimensionPixelOffset(R.dimen.drawable_padding), 0, 50, 0);
        checkBox.setLayoutParams(checkBoxParams);
        checkBox.setChecked(task.isIs_completed());
        checkBox.setEnabled(false);

        layout.addView(taskTextView);
        layout.addView(checkBox);
        taskListLayout.addView(layout);
    }

    private void importDataAPI(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Import Task Data");

        builder.setMessage("Are you sure you want to import all task?");
        builder.setPositiveButton("Import", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> titleExist = new ArrayList<>();
                fetchDataLocal().thenAccept(response -> {
                    for(final TodoModel todoModel : response){
                        titleExist.add(todoModel.getUsername()+"_"+todoModel.getId());
                    }
                    ApiConfigNstack.getRetrofitClient().getAllTodos(1,10).enqueue(new Callback<ResponseListApiTodoModel>() {
                        @Override
                        public void onResponse(Call<ResponseListApiTodoModel> call, Response<ResponseListApiTodoModel> response) {
                            if(response.isSuccessful()&&response.body()!=null){
                                ApiConfigNstack.getRetrofitClient().getAllTodos(1,response.body().getMeta().getTotal_items()).enqueue(new Callback<ResponseListApiTodoModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseListApiTodoModel> call, Response<ResponseListApiTodoModel> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            for (final ApiTodoModel apiTodoModel : response.body().getItems()){
                                                if (apiTodoModel.getTitle().contains(username)){
                                                    if (!titleExist.contains(apiTodoModel.getTitle())){
                                                        todoDAO.createTaskFromAPI(username,apiTodoModel.getDescription(),apiTodoModel.isIs_completed());
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseListApiTodoModel> call, Throwable throwable) {
                                        Log.e("MyTodos", "onFailure getAllTodos: ", throwable);
                                        Toast.makeText(getApplicationContext(), "There is an error or connection lost. Please try again later.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseListApiTodoModel> call, Throwable throwable) {
                            Log.e("MyTodos", "onFailure getAllTodos: ", throwable);
                            Toast.makeText(getApplicationContext(), "There is an error or connection lost. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    });
                }).exceptionally(throwable -> {
                    Log.e("MyTodos", "onFailure fetchDataLocal: ", throwable);
                    Toast.makeText(getApplicationContext(), "There is an error or connection lost. Please try again later.", Toast.LENGTH_LONG).show();
                    return null;
                });
                Toast.makeText(getApplicationContext(), "Import data successfully.", Toast.LENGTH_LONG).show();
                loadTasks();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private CompletableFuture<List<TodoModel>> fetchDataLocal(){
        CompletableFuture<List<TodoModel>> future = new CompletableFuture<>();

        List<TodoModel> tasks = todoDAO.getAllTasks(username);
        if (tasks != null) {
            future.complete(tasks);
        }else {
            future.completeExceptionally(new Exception("Response unsuccessful or body is null"));
        }
        return future;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyTodos.this, Home.class);
        startActivity(intent);
        finish();
    }
}