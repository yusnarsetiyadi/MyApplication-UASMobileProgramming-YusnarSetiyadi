package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.TodoModel;

import java.util.List;

public class Home extends AppCompatActivity {

    private TextView dropdownTitle, textAbout, selectedTask;
    private ListView dropdownListView;
    private Button addButton, guideButton;
    private static final int ACTIVITY_REQUEST_CODE = 1000;
    private static final int PERMISSION_REQUEST_CODE = 2000;
    private static final int REQUEST_LOCATION = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private FusedLocationProviderClient fusedLocationClient;
    private LinearLayout taskListLayout;
    private TodoModel.TodoDAO todoDAO;
    private String username,id,name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        id = sharedPreferences.getString("id","");
        username = sharedPreferences.getString("username", "");
        name = sharedPreferences.getString("name","");

        if (name != null) {
            TextView usernameTextView = findViewById(R.id.tekshome);
            usernameTextView.setText("Hello, " + name);
        }

        textAbout = findViewById(R.id.textAbout);
        textAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, About.class);
                Home.this.startActivity(intent);
                finish();
            }
        });

        dropdownTitle = findViewById(R.id.dropdownTitle);
        dropdownListView = findViewById(R.id.dropdownListView);

        String[] items = {"Cam", "Maps", "Logout"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        dropdownListView.setAdapter(adapter);
        dropdownListView.setVisibility(View.GONE);
        setDrawableLeft(R.drawable.baseline_menu_24);

        dropdownTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropdownListView.getVisibility() == View.VISIBLE) {
                    dropdownListView.setVisibility(View.GONE);
                    setDrawableLeft(R.drawable.baseline_menu_24);
                } else {
                    dropdownListView.setVisibility(View.VISIBLE);
                    setDrawableLeft(R.drawable.baseline_close_24);
                }
            }
        });

        dropdownListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                if (selectedItem=="Cam"){
                    dropdownListView.setVisibility(View.GONE);
                    setDrawableLeft(R.drawable.baseline_menu_24);
                    checkPermissionAndOpenCamera();
                }else if (selectedItem=="Maps"){
                    dropdownListView.setVisibility(View.GONE);
                    setDrawableLeft(R.drawable.baseline_menu_24);
                    checkPermissionAndOpenMaps();
                }else if (selectedItem=="Logout"){
                    dropdownListView.setVisibility(View.GONE);
                    setDrawableLeft(R.drawable.baseline_menu_24);
                    Toast.makeText(getApplicationContext(),
                            "successfully logout", Toast.LENGTH_LONG).show();
                    editor.remove("username");
                    editor.apply();
                    Intent intent = new Intent(Home.this, Login.class);
                    Home.this.startActivity(intent);
                    finish();
                }
            }
        });

        guideButton = findViewById(R.id.guide);
        guideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Home.this);
                builder.setTitle("How to use this ToDo?");
                builder.setMessage
                        ("Create todo: click add button\nEdit todo: click task\nDelete todo: hold task\nComplete todo: click checkbox").setNegativeButton
                        ("ok", null).create().show();
            }
        });

        addButton = findViewById(R.id.add_button);
        taskListLayout = findViewById(R.id.task_list_layout);
        sharedPreferences = getSharedPreferences("MyTasks", Context.MODE_PRIVATE);

        todoDAO = new TodoModel.TodoDAO(this);
        todoDAO.open();

        loadTasks();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });
    }

    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,ACTIVITY_REQUEST_CODE);
    }

    private void checkPermissionAndOpenMaps() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            openMapsDialog(latitude, longitude);
                        } else {
                            Toast.makeText(Home.this, "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Home.this, "Failed to get location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openMapsDialog(double latitude, double longitude) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.maps_dialog_input, null);

        final EditText inputDst = dialogView.findViewById(R.id.input_dst);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("Open Maps", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dst = inputDst.getText().toString();

                Uri uri = Uri.parse("https://www.google.com/maps/dir/" + latitude + "," + longitude + "/" + dst);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            if (capturedImage != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_image_preview, null);
                ImageView imageView = dialogView.findViewById(R.id.dialogImageView);
                imageView.setImageBitmap(capturedImage);
                builder.setView(dialogView)
                        .setTitle("Image Preview")
                        .setNegativeButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveImageToStorage(capturedImage);
                                Toast.makeText(Home.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Toast.makeText(Home.this, "Gagal mengambil gambar!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    checkPermissionAndOpenCamera();
                }
                break;
            case REQUEST_LOCATION:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    checkPermissionAndOpenMaps();
                }
                break;
        }
    }

    private void setDrawableLeft(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        dropdownTitle.setCompoundDrawables(drawable, null, null, null);
    }

    private void saveImageToStorage(Bitmap bitmap) {
        // Save image to gallery or specific directory
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "Image_" + System.currentTimeMillis(),
                "Image saved from My Application - UAS Mobile Programming - Yusnar Setiyadi"
        );

        // Show a toast message with the saved image URL or status
        if (savedImageURL != null) {
            Log.d("Save Image", "Image saved to: " + savedImageURL);
        } else {
            Log.d("Save Image", "Failed to save image.");
        }
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String task = input.getText().toString();
                if (!task.isEmpty()) {
                    saveTask(task);
                }
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

    private void saveTask(String task) {
        todoDAO.createTask(username,task);
        loadTasks();
    }

    private void loadTasks() {
        taskListLayout.removeAllViews();
        List<TodoModel> tasks = todoDAO.getAllTasks(username);
        if (tasks != null) {
            for (final TodoModel task : tasks) {
                addTaskView(task);
            }
        }

    }

    private void addTaskView(final TodoModel task) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        final TextView taskTextView = new TextView(this);
        taskTextView.setText(task.getTask());
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
        if (task.isCompleted()) {
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
        checkBox.setChecked(task.isCompleted());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleTaskCompletion(task);
            }
        });

        layout.addView(taskTextView);
        layout.addView(checkBox);
        taskListLayout.addView(layout);

        taskTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTaskDialog(task);
            }
        });

        taskTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteTaskDialog(task);
                return true;
            }
        });
    }

    private void showEditTaskDialog(final TodoModel task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        final EditText input = new EditText(this);
        input.setText(task.getTask());
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editedTask = input.getText().toString();
                if (!editedTask.isEmpty()) {
                    task.setTask(editedTask);
                    todoDAO.updateTask(task);
                    loadTasks();
                }
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

    private void showDeleteTaskDialog(final TodoModel task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");

        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                todoDAO.deleteTask(task);
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

    private void toggleTaskCompletion(TodoModel task) {
        task.setCompleted(!task.isCompleted());
        todoDAO.updateTask(task);
        loadTasks();
    }

    @Override
    protected void onDestroy() {
        todoDAO.close();
        super.onDestroy();
    }
}
