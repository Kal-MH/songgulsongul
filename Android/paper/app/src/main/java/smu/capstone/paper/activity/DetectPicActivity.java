package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import smu.capstone.paper.R;

public class DetectPicActivity extends AppCompatActivity {

    String filePath;
    ImageView detect_pic_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_pic);

        detect_pic_imageView = findViewById(R.id.detect_pic_iv);
        filePath = getIntent().getStringExtra("path");
        Glide.with(this).load(filePath).into(detect_pic_imageView);


    }
}