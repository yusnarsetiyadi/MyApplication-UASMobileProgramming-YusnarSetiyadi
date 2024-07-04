package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.api.ApiConfigJsonplace;
import com.my.myapplication_uasmobileprogramming_yusnarsetiyadi.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText username, password, editTextPassword;
    private TextView textAbout;
    Button btnlogin,buttonShowHidePassword;
    String keynama, keypass;
    private boolean isPasswordVisible = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.contains("username")) {
            String savedUsername = sharedPreferences.getString("username", "");
            if (!savedUsername.isEmpty()) {
                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);
                finish();
            }
        }

        textAbout = findViewById(R.id.textAbout);
        textAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, About.class);
                Login.this.startActivity(intent);
                finish();
            }
        });

        editTextPassword = findViewById(R.id.editekpassword);
        buttonShowHidePassword = findViewById(R.id.buttonShowHidePassword);

        buttonShowHidePassword.setOnClickListener(view->{
            if (isPasswordVisible) {
                editTextPassword.setInputType(editTextPassword.getInputType() | 0x00000001);
                buttonShowHidePassword.setBackgroundResource(R.drawable.eye_solid);
                isPasswordVisible = false;
            } else {
                editTextPassword.setInputType(editTextPassword.getInputType() & ~0x00000001);
                buttonShowHidePassword.setBackgroundResource(R.drawable.eye_slash_solid);
                isPasswordVisible = true;
            }
            editTextPassword.setSelection(editTextPassword.getText().length());
        });

        btnlogin = findViewById(R.id.tombollogin);
        btnlogin.setOnClickListener(view -> {

            username = findViewById(R.id.editekuser);
            password = findViewById(R.id.editekpassword);
            btnlogin = findViewById(R.id.tombollogin);

            keynama = username.getText().toString();
            keypass = password.getText().toString();

            validateUsers(keynama,keypass);
        });
    }

    private void validateUsers(String keynama, String keypass){
        ApiConfigJsonplace.getRetrofitClient().getAllUsers().enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null){
                    boolean loginSuccess = false;
                    String id = "",userName = "",name = "";
                    for (UserModel user : response.body()) {
                        if (user.getUsername().equals(keynama) && keypass.equals("password123")) {
                            loginSuccess = true;
                            id = user.getId().toString();
                            userName = user.getUsername();
                            name = user.getName();
                            break;
                        }
                    }
                    if (keynama.equals("")||keypass.equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage
                                ("username or password cannot be empty").setNegativeButton
                                ("repeat", null).create().show();

                        username.setText("");
                        password.setText("");
                    }else if (loginSuccess) {
                        Toast.makeText(getApplicationContext(), "successfully login", Toast.LENGTH_LONG).show();
                        editor.putString("id",id);
                        editor.putString("username",userName);
                        editor.putString("name",name);
                        editor.apply();
                        Intent intent = new Intent(Login.this, Home.class);
                        Login.this.startActivity(intent);
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage("incorrect username or password").setNegativeButton("repeat", null).create().show();
                        username.setText("");
                        password.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable throwable) {
                Log.e("Login", "onFailure getAllUsers: ", throwable);
                Toast.makeText(getApplicationContext(), "There is an error or connection lost. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }
}