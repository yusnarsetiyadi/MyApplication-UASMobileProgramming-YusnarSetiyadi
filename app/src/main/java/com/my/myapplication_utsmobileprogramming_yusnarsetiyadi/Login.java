package com.my.myapplication_utsmobileprogramming_yusnarsetiyadi;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button btnlogin;
    String keynama, keypass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin = findViewById(R.id.tombollogin);
        btnlogin.setOnClickListener(view -> {

            username = findViewById(R.id.editekuser);
            password = findViewById(R.id.editekpassword);
            btnlogin = findViewById(R.id.tombollogin);

            keynama = username.getText().toString();
            keypass = password.getText().toString();

            if (keynama.equals("")||keypass.equals("")){
                // jika field masih kosong
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setMessage
                        ("Username atau password tidak boleh kosong").setNegativeButton
                        ("ulangi", null).create().show();

                username.setText("");
                password.setText("");
            } else if (keynama.equals("YusnarSetiyadi") && keypass.equals("password")) {
                //jika login berhasil
                Toast.makeText(getApplicationContext(),
                        "LOGIN BERHASIL", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Login.this, Home.class);
                intent.putExtra("username",keynama);
                Login.this.startActivity(intent);
                finish();
            } else {
                //jika login gagal
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setMessage
                        ("Username atau password salah").setNegativeButton
                        ("ulangi", null).create().show();

                username.setText("");
                password.setText("");
            }

        });
    }
}