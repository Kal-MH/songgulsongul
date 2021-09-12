package com.smu.songgulsongul.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.smu.songgulsongul.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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

        back.setOnClickListener(new View.OnClickListener() {
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
                File file = new File(filePath);
                Uri uri = FileProvider.getUriForFile(EditDoneActivity.this,
                        "com.smu.songgulsongul.fileprovider",
                        file);
                shareIntent.setDataAndType(uri, getContentResolver().getType(uri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                Intent chooser = Intent.createChooser(shareIntent, "Share File");

                Log.d("TAG",filePath);

                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                startActivity(chooser);
            }
        });

    }
}