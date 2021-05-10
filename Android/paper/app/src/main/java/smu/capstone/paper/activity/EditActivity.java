package smu.capstone.paper.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import smu.capstone.paper.R;

public class EditActivity extends AppCompatActivity {

    String filePath;
    String sourceFilePath;
    ImageView edit_iv;
    Button done;
    Toolbar toolbar;


    LinearLayout editRatio;


    long first_time = 0;
    long second_time = 0;

    long croppedImageAddress;
    long paperImageAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        done = findViewById(R.id.edit_done);


        //툴바 세팅
        toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Step 3");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기


        editRatio = findViewById(R.id.edit_image_ratio);


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


        editRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent( EditActivity.this , EditImageRatioActivity.class);
                //intent.putExtra("path", filePath);
                //intent.putExtra("imageAddress",);
                startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.undo_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: // 뒤로가기 버튼 눌렀을 때
                // 알림팝업

                return true;

            case R.id.toolbar_undo : // 원본으로 복구


                return true;




        }
        return  true;
    }

}