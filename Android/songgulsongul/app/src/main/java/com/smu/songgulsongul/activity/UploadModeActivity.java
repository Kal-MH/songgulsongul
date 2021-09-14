package com.smu.songgulsongul.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.smu.songgulsongul.R;

public class UploadModeActivity extends Activity {

    ImageButton quickbtn;
    ImageButton normalbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_mode);

        quickbtn = findViewById(R.id.upload_mode_quick_btn);
        normalbtn = findViewById(R.id.upload_mode_normal_btn);


        quickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadModeActivity.this, UploadActivity.class);
                intent.putExtra("isQuick", true);
                startActivity(intent);
                finish();
            }
        });

        normalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadModeActivity.this, UploadActivity.class);
                intent.putExtra("isQuick", false);
                startActivity(intent);
                finish();
            }
        });


    }
}