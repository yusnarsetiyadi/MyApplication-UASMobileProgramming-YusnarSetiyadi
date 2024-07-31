package com.my.myapplication_uasmobileprogramming_yusnarsetiyadi;

import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import android.os.Bundle;
import java.io.File;

public class PDFViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        PDFView pdfView = findViewById(R.id.pdfView);

        pdfView.fromAsset("sample.pdf").load();
    }
}