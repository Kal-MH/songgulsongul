package com.smu.songgulsongul.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.smu.songgulsongul.R;

import java.io.File;

public class EditDoneActivity extends AppCompatActivity {
    Button upload, back, market_upload, share;
    String filePath;
    ImageView edit_done_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_done);

        upload = findViewById(R.id.edit_done_upload);
        back = findViewById(R.id.edit_done_back);
        market_upload = findViewById(R.id.edit_done_market);
        share = findViewById(R.id.edit_done_share);

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

        market_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDoneActivity.this, MarketUploadActivity.class);
                intent.putExtra("path", filePath);
                startActivity(intent);
                finish();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "공유하기"));
            }
        });

    }
}