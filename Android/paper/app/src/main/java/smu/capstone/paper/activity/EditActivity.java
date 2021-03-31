package smu.capstone.paper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import smu.capstone.paper.R;

public class EditActivity extends AppCompatActivity {

    String filePath;
    ImageView edit_iv;
    Button back, done;



    long first_time = 0;
    long second_time = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        done = findViewById(R.id.edit_done);
        back = findViewById(R.id.edit_back);

        filePath = getIntent().getStringExtra("path");
        edit_iv = findViewById(R.id.edit_pic);
        Glide.with(this).load(filePath).into(edit_iv);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( EditActivity.this , EditDoneActivity.class);
                intent.putExtra("path", filePath);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if(second_time-first_time <2000){
            super.onBackPressed();
            finish();
        }
        else{
            Toast.makeText(this,"한번 더 누르면 편집을 종료합니다", Toast.LENGTH_SHORT).show();
            first_time = System.currentTimeMillis();
        }
    }
}