package com.my.myapplication_utsmobileprogramming_yusnarsetiyadi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

public class Home extends AppCompatActivity {

    private TextView dropdownTitle;
    private ListView dropdownListView;
    private ArrayAdapter<String> adapter;
    private static final int ACTIVITY_REQUEST_CODE = 1000;
    private static final int PERMISSION_REQUEST_CODE = 2000;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dropdownTitle = findViewById(R.id.dropdownTitle);
        dropdownListView = findViewById(R.id.dropdownListView);

        String[] items = {"Home", "About", "Contact","Camera","Maps"};

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        dropdownListView.setAdapter(adapter);

        dropdownListView.setVisibility(View.GONE);
        setDrawableLeft(R.drawable.baseline_keyboard_arrow_right_24);

        dropdownTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropdownListView.getVisibility() == View.VISIBLE) {
                    dropdownListView.setVisibility(View.GONE);
                    setDrawableLeft(R.drawable.baseline_keyboard_arrow_right_24);
                } else {
                    dropdownListView.setVisibility(View.VISIBLE);
                    setDrawableLeft(R.drawable.baseline_keyboard_arrow_down_24);
                }
            }
        });

        dropdownListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                if (selectedItem=="Camera"){
                    dropdownListView.setVisibility(View.GONE);
                    setDrawableLeft(R.drawable.baseline_keyboard_arrow_right_24);
                    checkPermissionAndOpenCamera();
                }else if (selectedItem=="Maps"){
                    dropdownListView.setVisibility(View.GONE);
                    setDrawableLeft(R.drawable.baseline_keyboard_arrow_right_24);
                    checkPermissionAndOpenMaps();
//                    openInputDialogMaps();
                }else{
                    Toast.makeText(Home.this, selectedItem,Toast.LENGTH_SHORT).show();
                }
            }
        });

        String username = getIntent().getStringExtra("username");

        if (username != null) {
            TextView usernameTextView = findViewById(R.id.tekshome);
            usernameTextView.setText("Hello, " + username);
        }
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
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        } else {
            Toast.makeText(Home.this, "Location not available", Toast.LENGTH_SHORT).show();
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.maps_dialog_input, null);

        final EditText inputDst = dialogView.findViewById(R.id.input_dst);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("Open Maps", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dst = inputDst.getText().toString();

                Uri uri = Uri.parse("https://www.google.com/maps/dir/"+latitude+","+longitude+"/"+dst);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
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
                Toast.makeText(Home.this, "Berhasil mengambil gambar!", Toast.LENGTH_SHORT).show();
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(capturedImage);
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
}