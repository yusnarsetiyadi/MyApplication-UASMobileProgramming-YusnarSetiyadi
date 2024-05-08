package com.my.myapplication_utsmobileprogramming_yusnarsetiyadi;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

public class Login extends AppCompatActivity {

    EditText username, password,editTextPassword;
    Button btnlogin,buttonShowHidePassword;
    String keynama, keypass;
    private boolean isPasswordVisible = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
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

        editTextPassword = findViewById(R.id.editekpassword);
        buttonShowHidePassword = findViewById(R.id.buttonShowHidePassword);

        buttonShowHidePassword.setOnClickListener(view->{
            if (isPasswordVisible) {
                editTextPassword.setInputType(editTextPassword.getInputType() | 0x00000001);
                isPasswordVisible = false;
            } else {
                editTextPassword.setInputType(editTextPassword.getInputType() & ~0x00000001);
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

            if (keynama.equals("")||keypass.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setMessage
                        ("username or password cannot be empty").setNegativeButton
                        ("repeat", null).create().show();

                username.setText("");
                password.setText("");
            } else if (keynama.equals("admin") && keypass.equals("admin")) {
                Toast.makeText(getApplicationContext(),
                        "successfully login", Toast.LENGTH_LONG).show();
                editor.putString("username", keynama);
                editor.apply();
                Intent intent = new Intent(Login.this, Home.class);
                Login.this.startActivity(intent);
                finish();
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setMessage
                        ("incorrect username or password").setNegativeButton
                        ("repeat", null).create().show();

                username.setText("");
                password.setText("");
            }

        });
    }
}