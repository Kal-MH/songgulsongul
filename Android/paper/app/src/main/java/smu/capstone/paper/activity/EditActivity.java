package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import smu.capstone.paper.R;

public class EditActivity extends AppCompatActivity {

    String filePath;
    ImageView edit_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        filePath = getIntent().getStringExtra("path");
        edit_iv = findViewById(R.id.edit_pic);

        Glide.with(this).load(filePath).into(edit_iv);

    }
}