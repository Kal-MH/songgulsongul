package com.smu.songgulsongul.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.smu.songgulsongul.R;

public class EditDoneActivity extends AppCompatActivity {
    Button upload, back;
    String filePath;
    ImageView edit_done_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_done);

        upload = findViewById(R.id.edit_done_upload);
        back = findViewById(R.id.edit_done_back);

        filePath = getIntent().getStringExtra("path");
        edit_done_pic = findViewById(R.id.edit_done_pic);
        Glide.with(this).load(filePath).into(edit_done_pic);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDoneActivity.this, UploadDetailActivity.class);
                intent.putExtra("path", filePath);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDoneActivity.this, UploadActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}