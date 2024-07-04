package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textViewWA = findViewById(R.id.wa);
        textViewWA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/6281398447822"));
                startActivity(intent);
            }
        });
        TextView textViewGmail = findViewById(R.id.gmail);
        textViewGmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mailto:yusnarsetiyadi150403@gmail.com"));
                startActivity(intent);
            }
        });
        TextView textViewGithub = findViewById(R.id.github);
        textViewGithub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/yusnarsetiyadi"));
                startActivity(intent);
            }
        });
        TextView textViewLinkedIn = findViewById(R.id.linkedin);
        textViewLinkedIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.linkedin.com/in/yusnar-setiyadi-82b079252/"));
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.contains("username")) {
            Intent intent = new Intent(About.this, Home.class);
            About.this.startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(About.this, Login.class);
            About.this.startActivity(intent);
            finish();
        }
    }
}