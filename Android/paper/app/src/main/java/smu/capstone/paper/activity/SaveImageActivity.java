package smu.capstone.paper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import smu.capstone.paper.R;

public class SaveImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_save_image);

        ImageView image = (ImageView)findViewById(R.id.save_img_pick);
        Button cancle_btn = (Button)findViewById(R.id.save_img_cancle);
        Button save_btn = (Button)findViewById(R.id.save_img_btn);
        Spinner save_img_size = (Spinner)findViewById(R.id.save_img_size);

        Intent intent = getIntent();
        Glide.with(SaveImageActivity.this).load(intent.getIntExtra("postImg", 1)).into(image);

        save_img_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SaveImageActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();
            }
        });

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int)(display.getWidth()* 0.9);
        int height = (int)(display.getHeight() * 0.9);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        //Intent intent = getIntent();
        //image.setImageResource(intent.getIntExtra("image", 1));

    }
}